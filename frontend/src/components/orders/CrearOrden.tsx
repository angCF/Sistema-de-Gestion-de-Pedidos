import { useState } from "react";
import './styles/crearOrden.css';
import ApiClient from '../../utils/ApiCliente';
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import type { OrdenItem } from "./orden";
import { BASE_URL } from "../../utils/apiConfig";
import useFetch from "../../hooks/useFetch";
import type { Producto } from "../products/producto";

const CrearOrden = () => {
    const [numDocumentoComprador, setNumDocumentoComprador] = useState<string>("");
    const [nombreComprador, setNombreComprador] = useState<string>("");
    const [itemsOrden, setItemsOrden] = useState<OrdenItem[]>([]);
    const [nuevoItem, setNuevoItem] = useState<OrdenItem>({} as OrdenItem);
    const [erroresForm, setErroresForm] = useState<string[]>([]);
    const { data: listaProductos } = useFetch<Producto[]>(`${BASE_URL}/producto`);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const nuevosErrores: string[] = [];

        if (!nombreComprador.trim()) {
            nuevosErrores.push("El nombre del cliente es obligatorio.");
        }

        if (!numDocumentoComprador.trim()) {
            nuevosErrores.push("El número de documento es obligatorio.");
        }

        if (itemsOrden.length === 0) {
            nuevosErrores.push("Debe agregar al menos un producto a la orden.");
        }

        if (nuevosErrores.length > 0) {
            setErroresForm(nuevosErrores);
            return;
        }

        setErroresForm([]);

        const nuevaOrden = {
            numDocumentoComprador, nombreComprador, idProductos: itemsOrden.map(item => ({
                idProducto: item.idProducto,
                cantidad: item.cantidad
            }))
        };

        try {
            console.log(nuevaOrden);
            const response = await ApiClient.post("/orden", nuevaOrden);
            console.log(response.status)
            if (response.status === 201 || response.status === 200) {
                Swal.fire("Éxito", "Orden creada correctamente", "success").then(() => {
                    navigate("/ordenes");
                });
            } else {
                Swal.fire("Error", "No se pudo guardar la orden", "error");
            }
        } catch (error: any) {
            console.log(error)
            if(error.status === 409){
                setItemsOrden([]);
            }
            Swal.fire("Error", error.response?.data?.message || "Error desconocido", "error");
        }
    };
    const handleCancel = () => {
        navigate('/ordenes');
    };

    const agregarItem = () => {
        if (nuevoItem.idProducto === 0) {
            Swal.fire("Opss...", "Debe seleccionar un producto", "warning");
            return;
        }

        if (nuevoItem.cantidad <= 0 || isNaN(nuevoItem.cantidad)) {
            Swal.fire("Opss...", "Debe ingresar una cantidad válida", "warning");
            return;
        }

        setItemsOrden([...itemsOrden, nuevoItem]);
        setNuevoItem({ idProducto: 0, nombreProducto: "", cantidad: 1 });
    };

    return (
        <>
            <div className="modal-backdrop">
                <div className="modal-content-custom">
                    <div className="modal-header">
                        <h5 className="modal-title">Crear Orden</h5>
                        <button type="button" className="btn-close" onClick={handleCancel}></button>
                    </div>
                    <div className="modal-body-scrollable">
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label className="form-label">Nombre Cliente</label>
                                <input type="text" className="form-control" value={nombreComprador} onChange={(e) => setNombreComprador(e.target.value)} />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Numero Documento Cliente</label>
                                <input type="text" className="form-control" value={numDocumentoComprador} onChange={(e) => setNumDocumentoComprador(e.target.value)} />
                            </div>
                            {itemsOrden.length > 0 && (
                                <h6 className="mt-4 text-dark text-start">Productos incluidos</h6>
                            )}
                            <ul className="list-group mb-3">
                                {itemsOrden.map((item, index) => (
                                    <li key={index} className="list-group-item d-flex justify-content-between">
                                        <span>Producto:{item.nombreProducto} - Cantidad: {item.cantidad}</span>
                                    </li>
                                ))}
                            </ul>
                            <div className="mb-3">
                                <label className="form-label">Producto</label>
                                <select
                                    className="form-select"
                                    value={nuevoItem.idProducto}
                                    onChange={(e) => {
                                        const selectedId = parseInt(e.target.value);
                                        const productoSeleccionado = listaProductos.find(p => p.id === selectedId);
                                        setNuevoItem({
                                            ...nuevoItem,
                                            idProducto: selectedId,
                                            nombreProducto: productoSeleccionado?.nombre || ""
                                        });
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
                                    onChange={(e) => setNuevoItem({ ...nuevoItem, cantidad: parseInt(e.target.value) })}
                                />
                            </div>
                            <button type="button" className="btn btn-success mb-3" onClick={agregarItem}>Añadir a la orden</button>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={handleCancel}>Cancelar</button>
                                <button type="submit" className="btn btn-primary">Guardar</button>
                            </div>
                        </form>
                        {erroresForm.length > 0 && (
                            <div className="alert alert-danger">
                                <ul className="mb-0">
                                    {erroresForm.map((error, idx) => (
                                        <li key={idx}>{error}</li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default CrearOrden;