import { useNavigate, useParams } from "react-router-dom";
import useFetch from "../../hooks/useFetch";
import { BASE_URL } from "../../utils/apiConfig";
import type { Orden } from "./orden";
import Swal from "sweetalert2";

const VerOrden = () => {
    const { id } = useParams();
    const { data: ordenVer, loading, error } = useFetch<Orden>(`${BASE_URL}/orden/${id}`);
    const navigate = useNavigate();

    if (error) {
        Swal.fire("Error", "No se pudo cargar la orden", "error");
        navigate('/ordenes');
    }
    if (loading) return <p>Cargando...</p>;
    if (!ordenVer) {
        Swal.fire("Error", `No se encontró la orden con ID ${id}`, "error");
        navigate('/ordenes');
    }
    const handleClose = () => navigate("/ordenes");
    return (
        <>
            <div className="modal-backdrop">
                <div className="modal-content-custom">
                    <div className="modal-header">
                        <h4 className="modal-title w-100 text-center fw-bold">Ver Orden #{ordenVer.id}</h4>
                        <button type="button" className="btn-close" onClick={handleClose}></button>
                    </div>
                    <div className="modal-body-scrollable">
                        <h5 className="mb-3 fw-bold text-start">Datos del Cliente:</h5>
                        <div className="row mb-2">
                            <div className="col-6">
                                <span className="fw-bold text-start">Cliente:</span>
                                <span className="text-muted"> {ordenVer.nombreComprador}</span>
                            </div>
                            <div className="col-6">
                                <span className="fw-bold text-start">Documento:</span>
                                <span className="text-muted"> {ordenVer.numDocumentoComprador}</span>
                            </div>
                        </div>
                        <div className="row mb-2">
                            <div className="col-6">
                                <span className="fw-bold text-start">Precio total:</span>
                                <span className="text-muted"> ${ordenVer.precioCompra.toFixed(2)}</span>
                            </div>
                            <div className="col-6">
                                <span className="fw-bold text-start">Fecha:</span>
                                <span className="text-muted"> {new Date(ordenVer.fechaCompra).toLocaleDateString()}</span>
                            </div>
                        </div>

                        <h5 className="mt-4 fw-bold text-start">Productos incluidos:</h5>
                        <ul className="list-group">
                            {ordenVer.itemsOrden.map((item: any, i: number) => (
                                <li key={i} className="list-group-item d-flex justify-content-between align-items-center">
                                    <span>{item.nombreProducto}</span>
                                    <span className="badge bg-primary rounded-pill">x{item.cantidad}</span>
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div className="modal-footer">
                        <button className="btn btn-secondary" onClick={handleClose}>Cerrar</button>
                    </div>
                </div>
            </div>
        </>
    );
}
export default VerOrden;