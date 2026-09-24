'use strict';
const $ = id => document.getElementById(id);
const token = () => $('token').value.trim();
const csrfHeader = document.querySelector('meta[name="csrf-header"]').content;
const csrfToken = document.querySelector('meta[name="csrf-token"]').content;
function decode() {
  try {
    const part = token().split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const bytes = Uint8Array.from(atob(part.padEnd(Math.ceil(part.length / 4) * 4, '=')), c => c.charCodeAt(0));
    $('decoded').textContent = JSON.stringify(JSON.parse(new TextDecoder().decode(bytes)), null, 2);
  } catch { $('decoded').textContent = 'Sin token o formato no decodificable.'; }
  $('validation-status').textContent = 'Token modificado. Vuelve a validarlo en el servidor.';
  $('validation-status').className = 'result';
  $('validation-output').textContent = 'Sin validación para el token actual.';
}
async function request(path, options = {}) {
  const response = await fetch(path, {credentials: path.startsWith('/api/') ? 'omit' : 'same-origin', ...options});
  const raw = await response.text();
  let body; try { body = JSON.parse(raw); } catch { body = raw || '(sin cuerpo)'; }
  return {response, body};
}
function show(prefix, result) {
  const {response, body} = result;
  const label = response.status === 401 ? 'Identidad o token no válido' : response.status === 403 ? 'Petición no autorizada' : response.ok ? 'Petición aceptada' : 'Revisa la petición';
  $(prefix + '-status').textContent = `HTTP ${response.status} · ${label}`;
  $(prefix + '-status').className = `result ${response.ok ? 'good' : 'bad'}`;
  $(prefix + '-output').textContent = typeof body === 'string' ? body : JSON.stringify(body, null, 2);
}
async function guarded(button, action) {
  button.disabled = true;
  try { await action(); } catch (error) {
    $('api-status').textContent = 'No se pudo completar la petición. Comprueba que el servidor esté disponible.';
    $('api-status').className = 'result bad';
  } finally { button.disabled = false; }
}
$('token').addEventListener('input', decode);
$('issue').addEventListener('click', e => guarded(e.currentTarget, async () => {
  const result = await request('/lab/token?scenario=' + encodeURIComponent($('scenario').value), {
    method:'POST', headers:{[csrfHeader]:csrfToken, Accept:'application/json'}
  });
  if (!result.response.ok || !result.body.token) {
    $('token').value = ''; decode();
    $('validation-status').textContent = 'No se pudo emitir el token. Vuelve a iniciar sesión.';
    show('api', result); return;
  }
  $('token').value = result.body.token; decode();
  $('validation-status').textContent = 'JWT emitido. Pulsa Validar JWT para comprobarlo.';
}));
$('validate').addEventListener('click', e => guarded(e.currentTarget, async () => {
  show('validation', await request('/api/jwt/validar', {headers:token() ? {Authorization:'Bearer ' + token()} : {}}));
}));
$('tamper').addEventListener('click', () => {
  const parts = token().split('.');
  if (parts.length === 3 && parts[2]) {
    parts[2] = (parts[2][0] === 'A' ? 'B' : 'A') + parts[2].slice(1);
    $('token').value = parts.join('.'); decode();
  }
});
$('clear').addEventListener('click', () => { $('token').value = ''; decode(); });
document.querySelectorAll('[data-api]').forEach(button => button.addEventListener('click', () => guarded(button, async () => {
  const action = button.dataset.api;
  const id = encodeURIComponent($('request-id').value);
  let path = '/api/solicitudes', method = 'GET', body;
  if (action === 'public') path = '/api/public/info';
  if (action === 'create' || action === 'mass') { method = 'POST'; body = {descripcion:$('description').value}; }
  if (action === 'mass') Object.assign(body, {id:999, propietario:'supervisor', estado:'APROBADO'});
  if (action === 'edit') { method = 'PUT'; path += '/' + id; body = {descripcion:$('description').value}; }
  if (action === 'approve') { method = 'POST'; path += '/' + id + '/approve'; }
  const headers = {Accept:'application/json'};
  if (!['public','anonymous'].includes(action) && token()) headers.Authorization = 'Bearer ' + token();
  if (body) headers['Content-Type'] = 'application/json';
  show('api', await request(path, {method, headers, ...(body ? {body:JSON.stringify(body)} : {})}));
})));
