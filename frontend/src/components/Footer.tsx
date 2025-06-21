import React from "react";
import "../styles/footer.css";

const Footer: React.FC = () => {
    return (
        <footer className="footer bg-body-tertiary text-center text-muted">
            <div className="container py-3">
                <span>&copy; {new Date().getFullYear()} Sistema de Gestión de Pedidos</span>
            </div>
        </footer>
    );
};

export default Footer;