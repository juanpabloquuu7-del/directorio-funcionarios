const API = '/api/v1/funcionarios';
const $ = (id) => document.getElementById(id);

function mostrar(texto, tipo) {
  const m = $('mensaje');
  m.textContent = texto;
  m.className = tipo;
  m.hidden = false;
}

function pintar(lista) {
  const tbody = $('tabla');
  tbody.innerHTML = '';
  lista.forEach((f) => {
    const tr = document.createElement('tr');
    [f.id, f.nombre, f.cargo, f.departamento, f.telefono || ''].forEach((v) => {
      const td = document.createElement('td');
      td.textContent = v;
      tr.appendChild(td);
    });
    const acc = document.createElement('td');
    const editar = document.createElement('button');
    editar.textContent = 'Editar';
    editar.onclick = () => cargarEnFormulario(f);
    const borrar = document.createElement('button');
    borrar.textContent = 'Eliminar';
    borrar.className = 'peligro';
    borrar.onclick = () => eliminar(f);
    acc.append(editar, borrar);
    tr.appendChild(acc);
    tbody.appendChild(tr);
  });
}

async function llamar(url, opciones) {
  try {
    const res = await fetch(url, opciones);
    const texto = res.status === 204 ? '' : await res.text();
    let data = null;
    try { data = texto ? JSON.parse(texto) : null; } catch { data = null; }
    return { status: res.status, data };
  } catch {
    mostrar('Error de conexión con el servidor', 'error');
    return null;
  }
}

async function cargarLista(query = '', aviso = null) {
  const r = await llamar(API + query);
  if (!r) return;
  if (r.status === 200) {
    pintar(r.data);
    const n = r.data.length;
    mostrar(aviso || (query ? `Se encontraron ${n} resultado(s)` : `Listado completo: ${n} funcionario(s)`), 'ok');
  } else if (r.status === 204) {
    pintar([]);
    mostrar(query ? 'No se encontraron resultados' : 'El directorio está vacío', 'info');
  } else {
    mostrar('Ocurrió un error al consultar', 'error');
  }
}

function cargarEnFormulario(f) {
  $('id').value = f.id;
  $('nombre').value = f.nombre;
  $('cargo').value = f.cargo;
  $('departamento').value = f.departamento;
  $('telefono').value = f.telefono || '';
  $('tituloForm').textContent = 'Editar funcionario #' + f.id;
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function limpiar() {
  $('formulario').reset();
  $('id').value = '';
  $('tituloForm').textContent = 'Registrar funcionario';
}

$('formulario').addEventListener('submit', async (e) => {
  e.preventDefault();
  const id = $('id').value;
  const cuerpo = {
    nombre: $('nombre').value,
    cargo: $('cargo').value,
    departamento: $('departamento').value,
    telefono: $('telefono').value.trim() || null,
  };
  const r = await llamar(id ? `${API}/${id}` : API, {
    method: id ? 'PUT' : 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(cuerpo),
  });
  if (!r) return;
  if (r.status === 200 || r.status === 201) {
    limpiar();
    await cargarLista('', id ? 'Funcionario actualizado correctamente' : 'Funcionario registrado correctamente');
  } else if (r.status === 400) {
    mostrar((r.data && r.data.error) || 'Datos inválidos', 'error');
  } else if (r.status === 404) {
    mostrar('El funcionario ya no existe', 'error');
  } else {
    mostrar('Ocurrió un error al guardar', 'error');
  }
});

async function eliminar(f) {
  if (!confirm(`¿Eliminar a ${f.nombre}?`)) return;
  const r = await llamar(`${API}/${f.id}`, { method: 'DELETE' });
  if (!r) return;
  if (r.status === 204) {
    if ($('id').value == f.id) limpiar();
    await cargarLista('', 'Funcionario eliminado correctamente');
  } else if (r.status === 404) {
    mostrar('El funcionario no existe', 'error');
  } else {
    mostrar('Ocurrió un error al eliminar', 'error');
  }
}

$('btnId').onclick = async () => {
  const id = $('buscarId').value.trim();
  if (!id) return mostrar('Ingrese un ID', 'info');
  const r = await llamar(`${API}/${encodeURIComponent(id)}`);
  if (!r) return;
  if (r.status === 200) {
    pintar([r.data]);
    mostrar('Funcionario encontrado', 'ok');
  } else if (r.status === 404) {
    pintar([]);
    mostrar('No existe un funcionario con ese ID', 'info');
  } else {
    mostrar('Ocurrió un error al consultar', 'error');
  }
};
$('btnNombre').onclick = () => cargarLista('?nombre=' + encodeURIComponent($('buscarNombre').value));
$('btnDepto').onclick = () => cargarLista('?departamento=' + encodeURIComponent($('buscarDepto').value));
$('btnTodos').onclick = () => cargarLista();
$('limpiar').onclick = limpiar;

cargarLista();
