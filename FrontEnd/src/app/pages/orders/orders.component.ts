import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../service/order.service';
import { ActivatedRoute } from '@angular/router';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})

/**
 * Componente para la visualización de pedidos filtrados por usuario.
 * @description Este componente muestra una lista de pedidos filtrados según el correo electrónico del usuario almacenado en el local storage.
 */

export class OrdersComponent implements OnInit {

/**
   * Arreglo que contiene los pedidos filtrados por el correo electrónico del usuario.
   * @type {Order[]} Arreglo de objetos que representan los pedidos filtrados.
   */
  pedidosFiltrados: Order[] = [];
  orders: Order[] = [];
  userId: number;
  /**
   * Objeto que representa al usuario actual.
   * @type {any} Objeto que contiene la información del usuario.
   */
  usuario: any = null;

   /**
   * Correo electrónico utilizado como filtro para los pedidos.
   * @type {string} Correo electrónico del usuario utilizado para filtrar pedidos.
   */
  correoFiltro: string = '';

  /**
   * Nombre utilizado como filtro para los pedidos.
   * @type {string} Nombre del usuario utilizado para filtrar pedidos.
   */
  nombreFiltro: string = '';
 
  /**
   * Constructor del componente OrdersComponent.
   * @param {OrderService} orderService - Servicio para gestionar los pedidos.
   */
  constructor(private orderService: OrderService, private route: ActivatedRoute) {
    this.userId = 0;
  }

 /**
   * Método del ciclo de vida de Angular que se ejecuta al inicializar el componente.
   * Obtiene el usuario actual y los pedidos desde Firestore, y luego filtra los pedidos por el correo del usuario.
   */

 ngOnInit(): void {
  // Obtener el usuarioID desde localStorage o URL (según lo que prefieras)
  const userData = localStorage.getItem('sesionUsuario');
  if (userData) {
    const parsedUserData = JSON.parse(userData);
    this.userId = parsedUserData.id;  // Asumiendo que el ID de usuario está en el objeto del usuario

    // Llamamos al servicio para obtener los pedidos
    this.orderService.getOrdersByUserId(this.userId).subscribe({
      next: (response) => {
        this.orders = response.compras;  // Asegúrate de que la respuesta tenga la propiedad 'compras'
      },
      error: (err) => {
        console.error('Error al obtener los pedidos:', err);
      }
    });
  }
}
  /**
   * Método para filtrar los pedidos basado en el correo electrónico del usuario.
   * Actualiza el arreglo `pedidosFiltrados` con los pedidos que coinciden con el correo del usuario.
   */

  filtrarPedidos() {
    if (this.correoFiltro) {
      this.orderService.getOrdersByEmail(this.correoFiltro).subscribe(pedidos => {
        this.pedidosFiltrados = pedidos;
      });
    } else {
      this.pedidosFiltrados = [];
    }
  }

}