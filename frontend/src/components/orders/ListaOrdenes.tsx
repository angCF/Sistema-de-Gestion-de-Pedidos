import useFetch from '../../hooks/useFetch';
import '../../styles/orderList.css';
import { Link } from 'react-router-dom';
import { BASE_URL } from '../../utils/apiConfig';
import type { Orden } from './orden';
import Swal from 'sweetalert2';
import ApiCliente from '../../utils/ApiCliente';

const ListaOrdenes = () => {
  const { data: listOrdenes, error, loading, refetch } = useFetch<Orden[]>(`${BASE_URL}/orden`);

  const handleEliminar = async (id: number) => {
      const result = await Swal.fire({
          title: '¿Estás seguro?',
          text: 'Esta acción no se puede deshacer',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Sí, eliminar',
          cancelButtonText: 'Cancelar',
          allowOutsideClick: false,
          allowEscapeKey: false
      });

      if (result.isConfirmed) {
          try {
          const response = await ApiCliente.delete(`/orden/${id}`);
          if (response.status === 200 || response.status === 204) {
              await Swal.fire("Eliminado", "Orden eliminada correctamente", "success");
              refetch();
          } else {
              await Swal.fire("Error", "No se pudo eliminar la orden", "error");
          }
          } catch (error: any) {
          await Swal.fire("Error", error.response?.data?.message || "Error desconocido", "error");
          }
      }
  };

  return (
    <>
      {loading && (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '50vh' }}>
          <div className="spinner-border text-primary" role="status" style={{ width: '3rem', height: '3rem' }}>
            <span className="visually-hidden">Cargando...</span>
          </div>
        </div>
      )}

      {!loading && error && (
        <div className="text-center text-danger">
          <h2>Error al cargar las órdenes</h2>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && listOrdenes.length === 0 && (
        <div className="text-center">
          <h2>No hay órdenes disponibles</h2>
          <Link to="/crear-orden" className="btn btn-success">
            <span className="material-symbols-outlined">add_circle</span>
            <span>Añadir Orden</span>
          </Link>
        </div>
      )}

      {!loading && !error && listOrdenes.length > 0 && (
        <div className="table-responsive">
          <div className="table-title">
            <div className="row">
              <div className="col-xs-6">
                <h2 className="title-table">Lista de Órdenes</h2>
              </div>
              <div className="col-xs-6">
                <Link to="/crear-orden" className="btn btn-success">
                  <span className="material-symbols-outlined">add_circle</span>
                  <span>Añadir Orden</span>
                </Link>
              </div>
            </div>
          </div>
          <table className="table table-striped table-hover">
            <caption>Lista de Órdenes</caption>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre Comprador</th>
                <th>Precio Compra</th>
                <th>Fecha Compra</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {listOrdenes.map((orden) => (
                <tr key={orden.id}>
                  <td>{orden.numDocumentoComprador}</td>
                  <td>{orden.nombreComprador}</td>
                  <td>{orden.precioCompra}</td>
                  <td>{new Date(orden.fechaCompra).toLocaleDateString()}</td>
                  <td>
                    <Link to={`/ver-orden/${orden.id}`} className="icon-button">
                      <span className="material-symbols-outlined">visibility</span>
                    </Link>

                    <Link to={`/editar-orden/${orden.id}`} className="icon-button">
                      <span className="material-symbols-outlined">edit</span>
                    </Link>

                    <button
                      type="button"
                      className="icon-button"
                      onClick={() => handleEliminar(orden.id)}
                    >
                      <span className="material-symbols-outlined">delete</span>
                    </button>

                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
};

export default ListaOrdenes;
