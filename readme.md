# Taller diseño y estructuración de aplicaciones distribuidas en internet
Explorando las arquitecturas de aplicaciones distribuidas, específicamente en los servidores web que estan soportados sobre el protocolo HTTP, para este laboratorio aprendimos el uso de los sockets sobre una arquitectura ***cliente-servidor***, donde por medio de funciones Java creamos un servidor con sockets para atender a ciertas peticiones que nosotros le condicionamos, para este caso, el servidor debe traer los archivos del proyecto dada una URL o responder a nuestra aplicación web cuando el cliente interactue con ella.
## PreRequisitos
Para el proyecto debemos configurar un entorno maven para armar el proyecto, para esto hay diferentes opciones como visual studio, intellij, etc. Si se quiere trabajar sobre estos se debe configurar e instalar maven para su funcionamiento, pero, para este laboratorio y facilitarme este proceso utilicé Netbeans el cual me ahorra ese proceso, puede instalar el ambiente de desarrollo en su página oficial. También debemos tener java instalado y git para poder clonar el repositorio actual.
## Instalación
1. Vamos a clonar el repositorio de git con el siguiente comando
```bash
git clone https://github.com/Fataltester/Taller-1-AREP.git
```
2. Como estamos utilizando Netbeans simplemente abrimos el proyecto en el ambiente y ejecutamos el main, una vez iniciado, en la consola aparecerá "Listo para recibir ..."
<img width="1247" height="1035" alt="image" src="https://github.com/user-attachments/assets/a9f80cbf-e0f4-494d-abd3-30c1df290482" />
3. Procedemos a ir al navegador e insertar la siguiente dirección
```bash
http://localhost:35000/src/main/public/index.html
```
Lo que nos llevará a nuestra aplicación web
<img width="1214" height="946" alt="image" src="https://github.com/user-attachments/assets/0f90dda2-2bcf-4807-a8cb-4ab7ef649807" />

## Arquitectura
Estamos ante una arquitectura cliente-servidor, a nivel del carpetado del proyecto tenemos la siguiente estructura
<img width="492" height="370" alt="image" src="https://github.com/user-attachments/assets/16e613e0-56d0-46b2-8048-2917ea00e06d" />

## Pruebas

### Unitarias
<img width="723" height="301" alt="image" src="https://github.com/user-attachments/assets/a007950a-97ad-4032-9b42-156ca2cd4448" />

Donde 
1. verifica que el parámetro name de la URL se extraiga correctamente
2. En caso de que la URL no trae parámetro, se devuelve "Mundo"
3. Se verifica que se pueda detectar una extensión HTML
4. Se verifica que se pueda detectar una extensión de imagen
5. Se verifica que el método de leer el contenido de los archivos lo haga correctamente
6. Se verifica que se puede leer correctamente un arreglo de bytes
7. Se verifica que las peticiones HTTP se construyan correctamente
8. Cuando se tiene un tipo de archivo, se verifica que la cabecera HTTP contenga ese tipo de archivo
9. Verifica que las peticiones HTTP tambien se puedan construir con imagenes

### De aceptación

src/main/public/cat.jpg
<img width="1215" height="1000" alt="image" src="https://github.com/user-attachments/assets/d926a42c-06b1-4421-8bfd-39fc3d97236a" />

src/main/public/response.js
<img width="1212" height="943" alt="image" src="https://github.com/user-attachments/assets/ca6a0878-1c2e-4323-829e-494e2ff6ae10" />

src/main/public/index.html
<img width="1211" height="964" alt="image" src="https://github.com/user-attachments/assets/de1d247b-aabf-4f75-9570-ebc73b0217c7" />

Interacción con la aplicación
<img width="1215" height="961" alt="image" src="https://github.com/user-attachments/assets/2f546d12-b21a-4d3c-bdaf-d07d9b5dcdfb" />

### Contruido con 
[Maven](https://maven.apache.org) Maven

[Netbeans](https://netbeans.apache.org/front/main/index.html) Netbeans 

[Git](https://git-scm.com) Git

### Autor
Juan David Martínez Mendez - [Fataltester](https://github.com/Fataltester)







