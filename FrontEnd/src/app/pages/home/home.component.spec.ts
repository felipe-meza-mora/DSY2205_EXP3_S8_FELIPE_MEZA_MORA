import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { ProductService } from '../../service/product.service';
import { of } from 'rxjs';
import { Product } from '../../models/product.model';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let productService: jasmine.SpyObj<ProductService>;

  beforeEach(async () => {
    // Mock del modal en el DOM
    const modalElement = document.createElement('div');
    modalElement.id = 'deleteProductModal';
    document.body.appendChild(modalElement);

    // Mock de bootstrap.Modal
    (window as any).bootstrap = {
      Modal: {
        getInstance: jasmine.createSpy('getInstance').and.returnValue({
          hide: jasmine.createSpy('hide'),
        }),
      },
      Toast: jasmine.createSpy('Toast').and.returnValue({
        show: jasmine.createSpy('show'),
      }),
    };

    const productServiceMock = jasmine.createSpyObj('ProductService', [
      'getProducts',
      'addToCart',
      'deleteProduct',
      'updateProduct',
      'getCart',
      'updateCart',
      'clearCart',
      'addProduct',
    ]);

    productServiceMock.getProducts.and.returnValue(of({ products: [] }));
    productServiceMock.getCart.and.returnValue([]);

    await TestBed.configureTestingModule({
      imports: [HomeComponent], // Standalone component
      providers: [
        { provide: ProductService, useValue: productServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;

    fixture.detectChanges();
  });

  it('debería verificar los permisos de administrador correctamente', () => {
    const mockAdminUser = { permisos: 'ADMIN' };
    spyOn(localStorage, 'getItem').and.returnValue(JSON.stringify(mockAdminUser));
  
    component.ngOnInit();
  
    expect(component.isAdmin).toBeTrue();
  });

  it('debería vaciar el carrito correctamente', () => {
    component.clearCart();
  
    expect(productService.clearCart).toHaveBeenCalled();
    expect(component.cart.length).toBe(0);
    expect(component.total).toBe(0);
  });

  it('debería cargar productos al inicializar el componente', () => {
    const mockProducts: Product[] = [
      { id: 1, nombre: 'Producto 1', descripcion: 'Desc 1', precio: 100, imagen: 'img1.jpg' },
      { id: 2, nombre: 'Producto 2', descripcion: 'Desc 2', precio: 200, imagen: 'img2.jpg' },
    ];
  
    productService.getProducts.and.returnValue(of({ products: mockProducts }));
  
    component.ngOnInit();
  
    expect(productService.getProducts).toHaveBeenCalled();
    expect(component.products).toEqual(mockProducts);
  });

  it('debería calcular correctamente el total del carrito', () => {
    component.cart = [
      { product: { id: 1, nombre: 'Producto 1', descripcion: 'Desc 1', precio: 100, imagen: 'img1.jpg' }, quantity: 2 },
      { product: { id: 2, nombre: 'Producto 2', descripcion: 'Desc 2', precio: 200, imagen: 'img2.jpg' }, quantity: 1 },
    ];
  
    component.calculateTotal();
  
    expect(component.total).toBe(400); // 100 * 2 + 200 * 1
  });
  

  it('debería eliminar un producto y cerrar el modal', () => {
    const mockProduct: Product = { id: 1, nombre: 'Producto 1', descripcion: 'Desc 1', precio: 100, imagen: 'img1.jpg' };
    component.selectedProduct = mockProduct;
    productService.deleteProduct.and.returnValue(of(undefined));

    component.deleteProduct();

    const modalElement = document.getElementById('deleteProductModal');
    expect((window as any).bootstrap.Modal.getInstance).toHaveBeenCalledWith(modalElement);
    const modalInstance = (window as any).bootstrap.Modal.getInstance(modalElement);
    expect(modalInstance.hide).toHaveBeenCalled();
  });

});
