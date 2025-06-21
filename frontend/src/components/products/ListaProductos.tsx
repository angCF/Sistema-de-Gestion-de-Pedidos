import '../../styles/productList.css';
import useFetch from '../../hooks/useFetch';
import type { Producto } from './producto';
import { Link } from 'react-router-dom';
import { BASE_URL } from '../../utils/apiConfig';
import Swal from 'sweetalert2';
import ApiCliente from '../../utils/ApiCliente';

const ListaProductos = () => {
    const { data: listProducts, error, loading, refetch } = useFetch<Producto[]>(`${BASE_URL}/producto`);

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
                const response = await ApiCliente.delete(`/producto/${id}`);
                if (response.status === 200 || response.status === 204) {
                    await Swal.fire("Eliminado", "Producto eliminado correctamente", "success");
                    refetch();
                } else {
                    await Swal.fire("Error", "No se pudo eliminar el producto", "error");
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
                    <h2>Error al cargar los productos</h2>
                    <p>{error}</p>
                </div>
            )}

            {!loading && !error && listProducts.length === 0 && (
                <div className="text-center">
                    <h2>No hay productos disponibles</h2>
                    <Link to="/crear-producto" className="btn btn-success">
                        <span className="material-symbols-outlined">add_circle</span>
                        <span>Añadir Producto</span>
                    </Link>
                </div>
            )}

            {!loading && !error && listProducts.length > 0 && (
                <div className="table-responsive">
                    <div className="table-title">
                        <div className="row">
                            <div className="col-xs-6">
                                <h2 className="title-table">Lista de Productos</h2>
                            </div>
                            <div className="col-xs-6">
                                <Link to="/crear-producto" className="btn btn-success">
                                    <span className="material-symbols-outlined">add_circle</span>
                                    <span>Añadir Producto</span>
                                </Link>
                            </div>
                        </div>
                    </div>
                    <table className="table table-striped table-hover">
                        <caption>Lista de Productos</caption>
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Descripción</th>
                                <th>Precio de venta</th>
                                <th>Stock</th>
                                <th id="table-action">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listProducts.map(producto => (
                                <tr key={producto.id}>
                                    <td>{producto.nombre}</td>
                                    <td>{producto.descripcion}</td>
                                    <td>{producto.precioVenta}</td>
                                    <td>{producto.stock}</td>
                                    <td>
                                        <Link to={`/editar-producto/${producto.id}`} className="icon-button">
                                            <span className="material-symbols-outlined">edit</span>
                                        </Link>

                                        <button
                                            type="button"
                                            className="icon-button"
                                            onClick={() => handleEliminar(producto.id)}
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

export default ListaProductos;
