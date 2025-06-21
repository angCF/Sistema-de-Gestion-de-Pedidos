export interface OrdenItem {
  idProducto: number;
  nombreProducto: string;
  cantidad: number;
}

export interface Orden{
    id: number;
    numDocumentoComprador: string;
    nombreComprador: string;
    precioCompra: number;
    fechaCompra: Date;
    itemsOrden: OrdenItem[];
};