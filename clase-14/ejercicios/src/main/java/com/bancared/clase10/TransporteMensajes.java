package com.bancared.clase10;
import java.util.*;
import static com.bancared.clase10.EventosModelos.*;
public interface TransporteMensajes extends AutoCloseable {boolean publicar(Evento e);int consumir(int max,InboxStore inbox);void veneno(String clave);List<Map<String,Object>> vista();String modo();default void close(){} }
