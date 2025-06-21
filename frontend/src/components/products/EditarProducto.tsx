import './styles/crearProducto.css';
import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import ApiClient from '../../utils/ApiCliente';
import Swal from 'sweetalert2';
import useFetch from '../../hooks/useFetch';
import type { Producto } from './producto';
import { BASE_URL } from '../../utils/apiConfig';

const EditarProducto = () => {
  const [producto, setProducto] = useState<Producto>({} as Producto);

  const { id } = useParams();
  const navigate = useNavigate();

  const { data: productoActual, loading, error } = useFetch<Producto>(`${BASE_URL}/producto/${id}`);

  useEffect(() => {
    if (productoActual) {
      setProducto(productoActual);
    }
  }, [productoActual]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const productoActualizado = {
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precioVenta: producto.precioVenta,
      stock: producto.stock
    };

    try {
      const response = await ApiClient.put(`/producto/${id}`, productoActualizado);
      if (response.status === 200 || response.status === 204) {
        Swal.fire("Éxito", "Producto actualizado correctamente", "success").then(() => {
          navigate("/productos");
        });
      } else {
        Swal.fire("Error", "No se pudo actualizar el producto", "error");
      }
    } catch (error: any) {
      Swal.fire("Error", error.response?.data?.message || "Error desconocido", "error");
    }
  };

  const handleCancel = () => {
    navigate('/productos');
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
          <h2>Error al cargar el producto</h2>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && (
        <div className="modal-backdrop">
          <div className="modal-content-custom">
            <div className="modal-header">
              <h5 className="modal-title">Editar Producto</h5>
              <button type="button" className="btn-close" onClick={handleCancel}></button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">Nombre</label>
                  <input
                    type="text"
                    className="form-control"
                    value={producto.nombre}
                    onChange={(e) => setProducto({ ...producto, nombre: e.target.value })}
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Descripción</label>
                  <textarea
                    className="form-control"
                    rows={3}
                    value={producto.descripcion}
                    onChange={(e) => setProducto({ ...producto, descripcion: e.target.value })}
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Precio de venta</label>
                  <input
                    type="number"
                    className="form-control"
                    value={producto.precioVenta}
                    onChange={(e) => setProducto({ ...producto, precioVenta: parseFloat(e.target.value) })}
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Stock</label>
                  <input
                    type="number"
                    className="form-control"
                    value={producto.stock}
                    onChange={(e) => setProducto({ ...producto, stock: parseInt(e.target.value) })}
                  />
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary">
                    Guardar Cambios
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default EditarProducto;
