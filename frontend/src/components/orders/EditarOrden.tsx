import { useNavigate, useParams } from "react-router-dom";
import type { Orden, OrdenItem } from "./orden";
import ApiClient from '../../utils/ApiCliente';
import { useEffect, useState } from "react";
import { BASE_URL } from "../../utils/apiConfig";
import Swal from "sweetalert2";
import useFetch from "../../hooks/useFetch";
import type { Producto } from "../products/producto";

const EditarOrden = () => {
    const { id } = useParams();
    const [orden, setOrden] = useState<Orden>({} as Orden);
    const { data: listaProductos } = useFetch<Producto[]>(`${BASE_URL}/producto`);;
    const [nuevoItem, setNuevoItem] = useState<OrdenItem>({} as OrdenItem);
    const { data: ordenActual, loading, error } = useFetch<Orden>(`${BASE_URL}/orden/${id}`);
    const [erroresForm, setErroresForm] = useState<string[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        if (ordenActual) {
            setOrden(ordenActual);
        }
    }, [ordenActual]);

    const handleCantidadChange = (index: number, value: number) => {
        const nuevosItems = [...orden.itemsOrden];
        nuevosItems[index].cantidad = value;
        setOrden({ ...orden, itemsOrden: nuevosItems });
    };
    const eliminarProducto = (index: number) => {
        const nuevosItems = orden.itemsOrden.filter((_, i) => i !== index);
        setOrden({ ...orden, itemsOrden: nuevosItems });
    };
    const agregarItem = () => {
        if (!orden) return;
        const productoExistente = orden.itemsOrden.find(item => item.idProducto === nuevoItem.idProducto);
        if (productoExistente) {
            Swal.fire("Atención", "Este producto ya está en la orden", "warning");
            return;
        }
        const productoSeleccionado = listaProductos.find(p => p.id === nuevoItem.idProducto);
        const nuevo = {
            ...nuevoItem,
            nombreProducto: productoSeleccionado?.nombre || "Producto sin nombre"
        };
        setOrden({
            ...orden,
            itemsOrden: [...orden.itemsOrden, nuevo]
        });
        setNuevoItem({ idProducto: 0, nombreProducto: "", cantidad: 1 });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const errores: string[] = [];
        if (!orden.nombreComprador.trim()) {
            errores.push("El nombre del cliente es obligatorio.");
        }
        if (!orden.numDocumentoComprador.trim()) {
            errores.push("El número de documento es obligatorio.");
        }
        if (orden.itemsOrden.length === 0) {
            errores.push("Debe haber al menos un producto en la orden.");
        }

        setErroresForm(errores);
        if (errores.length > 0) return;

        const nuevaOrden = {
            nombreComprador: orden.nombreComprador,
            numDocumentoComprador: orden.numDocumentoComprador,
            idProductos: orden.itemsOrden.map((item) => ({
                idProducto: item.idProducto,
                cantidad: item.cantidad,
            })),
        };

        try {
            console.log(nuevaOrden);
            await ApiClient.put(`/orden/${id}`, nuevaOrden);
            Swal.fire("Éxito", "Orden actualizada correctamente", "success").then(() => {
                navigate("/ordenes");
            });
        } catch (error) {
            Swal.fire("Error", "No se pudo actualizar la orden");
        }
    };

    const handleCancel = () => navigate("/ordenes");

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
                            <h5 className="modal-title w-100 text-center">Editar Orden #{orden.id}</h5>
                            <button type="button" className="btn-close position-absolute end-0" onClick={handleCancel}></button>
                        </div>
                        <div className="modal-body-scrollable">
                            <form onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label className="form-label">Nombre Cliente</label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        value={orden.nombreComprador}
                                        onChange={(e) => setOrden({ ...orden, nombreComprador: e.target.value })}
                                    />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Número Documento Cliente</label>
                                    <textarea
                                        className="form-control"
                                        rows={2}
                                        value={orden.numDocumentoComprador}
                                        onChange={(e) => setOrden({ ...orden, numDocumentoComprador: e.target.value })}
                                    />
                                </div>

                                <h6 className="mt-4 text-dark text-start">Productos incluidos</h6>
                                {orden.itemsOrden?.map((item: OrdenItem, index: number) => (
                                    <div key={index} className="mb-3 border rounded p-3">
                                        <div className="d-flex justify-content-between align-items-center mb-2">
                                            <strong>{item.nombreProducto}</strong>
                                            <button
                                                type="button"
                                                className="btn btn-sm btn-outline-danger"
                                                onClick={() => eliminarProducto(index)}
                                            >
                                                Quitar
                                            </button>
                                        </div>
                                        <label className="form-label">Cantidad</label>
                                        <input
                                            type="number"
                                            className="form-control"
                                            value={item.cantidad}
                                            min={1}
                                            onChange={(e) => handleCantidadChange(index, parseInt(e.target.value))}
                                        />
                                    </div>
                                ))}
                                <h6 className="mt-4 text-dark text-start">Agregar nuevo producto</h6>
                                <div className="mb-3">
                                    <label className="form-label">Producto</label>
                                    <select
                                        className="form-select"
                                        value={nuevoItem.idProducto}
                                        onChange={(e) => {
                                            const selectedId = parseInt(e.target.value);
                                            setNuevoItem({ ...nuevoItem, idProducto: selectedId });
                                        }}
                                    >
                                        <option value={0}>Seleccione un producto</option>
                                        {listaProductos.map((producto) => (
                                            <option key={producto.id} value={producto.id}>
                                                {producto.nombre}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Cantidad</label>
                                    <input
                                        type="number"
                                        className="form-control"
                                        value={nuevoItem.cantidad}
                                        min={1}
                                        onChange={(e) => setNuevoItem({ ...nuevoItem, cantidad: parseInt(e.target.value) })}
                                    />
                                </div>
                                <button type="button" className="btn btn-success mb-4" onClick={agregarItem}>
                                    Añadir a la orden
                                </button>
                                {erroresForm.length > 0 && (
                                    <div className="alert alert-danger">
                                        <ul className="mb-0">
                                            {erroresForm.map((error, idx) => (
                                                <li key={idx}>{error}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                                        Cancelar
                                    </button>
                                    <button type="submit" className="btn btn-primary">
                                        Guardar
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}
export default EditarOrden;