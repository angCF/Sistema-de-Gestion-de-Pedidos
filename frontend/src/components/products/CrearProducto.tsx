import './styles/crearProducto.css';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import ApiClient from '../../utils/ApiCliente';
import Swal from 'sweetalert2';

const CrearProducto = () => {
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [precioVenta, setPrecioVenta] = useState(0);
  const [stock, setStock] = useState(0);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const nuevoProducto = { nombre, descripcion, precioVenta, stock };

    try {
      const response = await ApiClient.post("/producto", nuevoProducto);
      console.log(response.status)
      if (response.status === 201 || response.status === 200) {
        Swal.fire("Éxito", "Producto creado correctamente", "success").then(() => {
          navigate("/productos");
        });
      } else {
        Swal.fire("Error", "No se pudo guardar el producto", "error");
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
      <div className="modal-backdrop">
        <div className="modal-content-custom">
          <div className="modal-header">
            <h5 className="modal-title">Añadir Producto</h5>
            <button type="button" className="btn-close" onClick={handleCancel}></button>
          </div>
          <div className="modal-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Nombre</label>
                <input type="text" className="form-control" value={nombre} onChange={(e) => setNombre(e.target.value)} />
              </div>
              <div className="mb-3">
                <label className="form-label">Descripción</label>
                <textarea className="form-control" rows={3} value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />
              </div>
              <div className="mb-3">
                <label className="form-label">Precio de venta</label>
                <input type="number" className="form-control" value={precioVenta} onChange={(e) => setPrecioVenta(parseFloat(e.target.value))} />
              </div>
              <div className="mb-3">
                <label className="form-label">Stock</label>
                <input type="number" className="form-control" value={stock} onChange={(e) => setStock(parseInt(e.target.value))} />
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancelar</button>
                <button type="submit" className="btn btn-primary">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </>
  );
};

export default CrearProducto;
