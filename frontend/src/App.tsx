import './App.css'
import Header from './components/Header'
import { HashRouter, Route, Routes, Navigate } from 'react-router-dom'
import NoFound from './utils/NoFound'

const App = () => {
  return (
    <>      
      <HashRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/productos" />} />
          <Route path="/productos" element={<Header ruta="productos" />} />
          <Route path="/crear-producto" element={<Header ruta="crear-producto" />} />
          <Route path="/editar-producto/:id" element={<Header ruta="editar-producto" />} />

          <Route path="/ordenes" element={<Header ruta="ordenes" />} />
          <Route path="/crear-orden" element={<Header ruta="crear-orden" />} />
          <Route path="/ver-orden/:id" element={<Header ruta="ver-orden" />} />
          <Route path="/editar-orden/:id" element={<Header ruta="editar-orden" />} />

          <Route path="/noFound" element={<NoFound />} />
        </Routes>
      </HashRouter>
    </>
  )
}

export default App;