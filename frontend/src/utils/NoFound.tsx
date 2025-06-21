import Header from "../components/Header"

const NoFound = () => {
    return (
        <body className="noFound">
            <div>
            <Header />
            </div>
            <div className="container" >
                <div className="row">
                    <div className="col-md-12">
                        <div className="error-template">
                            <h1>
                                Oops!</h1>
                            <h2>
                                Acceso no Autorizado</h2>
                            <div className="error-details">
                                Lo siento, No tienes acceso a esta página
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>
    )
}

export default NoFound