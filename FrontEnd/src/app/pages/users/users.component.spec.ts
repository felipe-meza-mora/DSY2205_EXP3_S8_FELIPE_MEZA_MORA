import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UsersComponent } from './users.component';
import { UsersService } from '../../service/users.service';
import { of, throwError } from 'rxjs';
import { User } from '../../models/users.model';

describe('UsersComponent', () => {
  let component: UsersComponent;
  let fixture: ComponentFixture<UsersComponent>;
  let usersService: jasmine.SpyObj<UsersService>;

  beforeEach(async () => {
    const usersServiceMock = jasmine.createSpyObj('UsersService', ['getUsers', 'deleteUser']);
  
    // Configurar valores por defecto para los métodos
    usersServiceMock.getUsers.and.returnValue(of([])); // Simula un array vacío de usuarios
    usersServiceMock.deleteUser.and.returnValue(of(undefined)); // Simula una eliminación exitosa
  
    // Configurar el TestBed
    await TestBed.configureTestingModule({
      imports: [UsersComponent], // Cambiado a imports porque es standalone
      providers: [{ provide: UsersService, useValue: usersServiceMock }],
    }).compileComponents();
  
    fixture = TestBed.createComponent(UsersComponent);
    component = fixture.componentInstance;
    usersService = TestBed.inject(UsersService) as jasmine.SpyObj<UsersService>;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar la lista de usuarios al inicializar', () => {
    const mockUsers: User[] = [
      { id: 1, nombre: 'Usuario 1', correo: 'usuario1@example.com', telefono: '123456789', direccionEnvio: 'Calle 1', permisos: 'USER', rut: '12345678-9', password: 'password123' },
      { id: 2, nombre: 'Usuario 2', correo: 'usuario2@example.com', telefono: '987654321', direccionEnvio: 'Calle 2', permisos: 'ADMIN', rut: '98765432-1', password: 'password456' },
    ];

    usersService.getUsers.and.returnValue(of(mockUsers));
    component.loadUsers();

    expect(usersService.getUsers).toHaveBeenCalled();
    expect(component.users).toEqual(mockUsers);
  });

  it('debería manejar un error al cargar usuarios', () => {
    const mockError = new Error('Error al cargar usuarios');
    usersService.getUsers.and.returnValue(throwError(() => mockError)); // Simula un error
  
    component.loadUsers();
  
    expect(usersService.getUsers).toHaveBeenCalled();
    expect(component.users).toEqual([]); // La lista debería permanecer vacía en caso de error
  });

  it('debería mostrar un mensaje al eliminar un usuario', () => {
    spyOn(component as any, 'showToast').and.callThrough();

    const mockUser: User = {
      id: 1,
      nombre: 'Usuario 1',
      correo: 'usuario1@example.com',
      telefono: '123456789',
      direccionEnvio: 'Calle 1',
      permisos: 'USER',
      rut: '12345678-9',
      password: 'password123',
    };

    usersService.deleteUser.and.returnValue(of(void 0));
    component.selectedUser = mockUser;
    component.deleteUser();

    expect((component as any).showToast).toHaveBeenCalledWith(`${mockUser.nombre} ha sido eliminado.`);
  });
  
  it('debería manejar un error al eliminar un usuario', () => {
    const mockUser: User = {
      id: 1,
      nombre: 'Usuario 1',
      correo: 'usuario1@example.com',
      telefono: '123456789',
      direccionEnvio: 'Calle 1',
      permisos: 'USER',
      rut: '12345678-9',
      password: 'password123',
    };
  
    const mockError = new Error('Error al eliminar usuario');
    usersService.deleteUser.and.returnValue(throwError(() => mockError)); // Simula un error
    component.selectedUser = mockUser;
  
    component.deleteUser();
  
    expect(usersService.deleteUser).toHaveBeenCalledWith(mockUser.id);
  });

  it('debería cargar usuarios automáticamente al inicializar', () => {
    spyOn(component, 'loadUsers');
  
    component.ngOnInit();
  
    expect(component.loadUsers).toHaveBeenCalled();
  });

  it('debería no realizar ninguna acción si el usuario seleccionado no tiene un ID válido', () => {
    const invalidUser: User = {
      id: null as any, // Usuario sin un ID válido
      nombre: 'Usuario inválido',
      correo: 'usuario@example.com',
      telefono: '123456789',
      direccionEnvio: 'Calle 1',
      permisos: 'USER',
      rut: '12345678-9',
      password: 'password123',
    };
  
    component.selectedUser = invalidUser;
    component.deleteUser();
  
    expect(usersService.deleteUser).not.toHaveBeenCalled();
  });
  

});
