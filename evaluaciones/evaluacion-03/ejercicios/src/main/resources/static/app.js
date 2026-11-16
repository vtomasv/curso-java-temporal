(() => {
    const normalize = (value) => value
        .toLocaleLowerCase("es")
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "");

    const filter = document.querySelector("[data-order-filter]");
    if (filter) {
        const rows = [...document.querySelectorAll("[data-order-row]")];
        const empty = document.querySelector("[data-filter-empty]");
        const applyFilter = () => {
            const query = normalize(filter.value.trim());
            let visible = 0;
            rows.forEach((row) => {
                const matches = normalize(row.dataset.search || "").includes(query);
                row.hidden = !matches;
                if (matches) visible += 1;
            });
            if (empty) empty.hidden = visible !== 0 || rows.length === 0;
        };
        filter.addEventListener("input", applyFilter);
    }

    const content = document.querySelector("[data-character-source]");
    const counter = document.querySelector("[data-character-count]");
    if (content && counter) {
        const updateCounter = () => {
            counter.textContent = String(content.value.length);
            const fieldCounter = counter.closest(".field-counter");
            if (fieldCounter) fieldCounter.classList.toggle("counter-warning", content.value.length > 1900);
        };
        content.addEventListener("input", updateCounter);
        updateCounter();
    }

    document.querySelectorAll("[data-copy-target]").forEach((button) => {
        button.addEventListener("click", async () => {
            const target = document.querySelector(button.dataset.copyTarget);
            if (!target) return;
            const previous = button.textContent;
            try {
                if (!navigator.clipboard) throw new Error("Clipboard API no disponible");
                await navigator.clipboard.writeText(target.textContent.trim());
                button.textContent = "Copiado";
            } catch {
                button.textContent = "No se pudo copiar";
            }
            window.setTimeout(() => { button.textContent = previous; }, 1600);
        });
    });

    document.querySelectorAll("form[data-confirm]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            if (!window.confirm(form.dataset.confirm)) event.preventDefault();
        });
    });
})();
