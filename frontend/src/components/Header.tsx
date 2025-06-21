import { Fragment } from "react/jsx-runtime";
import { Link } from "react-router-dom";
import CrearProducto from "./products/CrearProducto";
import EditarProducto from "./products/EditarProducto";
import ListaProductos from "./products/ListaProductos";
import ListaOrdenes from "./orders/ListaOrdenes";
import CrearOrden from "./orders/CrearOrden";
import VerOrden from "./orders/VerOrden";
import EditarOrden from "./orders/EditarOrden";

const Header = (props: { ruta?: string; }) => {
    return (
        <Fragment>
            <header>
                <nav className="navbar navbar-expand-lg bg-body-tertiary">
                    <div className="container-fluid">
                        <a className="navbar-brand" href="#"><img src="/vite.svg" alt="Logo" width="60" height="50" className="d-inline-block align-text-top"/></a>
                        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                <li className="nav-item">
                                    <Link to="/productos" className="nav-link active" aria-current="page">
                                    Productos
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/ordenes" className="nav-link active" aria-current="page">
                                    Ordenes
                                    </Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </header>
            <div className="container">
                {
                    props.ruta === "productos" ? <ListaProductos /> : ""
                }
                {
                    props.ruta === "crear-producto" ? <CrearProducto /> : ""
                }
                {
                    props.ruta === "editar-producto" ? <EditarProducto /> : ""
                }
                {
                    props.ruta === "ordenes" ? <ListaOrdenes /> : ""
                }
                {
                    props.ruta === "crear-orden" ? <CrearOrden /> : ""
                }
                {
                    props.ruta === "ver-orden" ? <VerOrden /> : ""
                }
                {
                    props.ruta === "editar-orden" ? <EditarOrden /> : ""
                }
            </div>
        </Fragment>
    );
};

export default Header;