package com.distribuida.rest;

import com.distribuida.clientes.authors.AuthorRestProxy;
import com.distribuida.clientes.authors.AuthorsCliente;
import com.distribuida.db.Book;
import com.distribuida.dtos.BookDto;
import com.distribuida.servicios.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
        info = @Info(
                title = "API Book",
                version = "1.0.0"
        ),
        tags = {
                @Tag(name = "Prueba OpenAPI")
        }
)
public class BookRest {
    @Inject BookRepository bookService;
    @RestClient
    @Inject AuthorRestProxy proxyAuthor;
    /**
     * GET          /books/{id}     buscar un libro por ID
     * GET          /books          listar todos
     * POST         /books          insertar
     * PUT/PATCH    /books/{id}     actualizar
     * DELETE       /books/{id}     eliminar
     */
    @GET
    @Operation(summary = "Retorna una lista de objetos",
            description = "Return List<Book>")
    @APIResponse(responseCode = "404", description = "No hay Books")
    public List<Book> findAll() {
        return bookService.findAll();
    }
    @GET
    @Path("/{id}")
    @Operation(summary = "Retorna un objeto",
            description = "Return Book")
    @APIResponse(responseCode = "404", description = "Book no encontrado")
    public Book findById(@PathParam("id") Integer id) {
        return bookService.findById(id);
    }
    @GET
    @Path("/all")
    public List<BookDto> findAllCompleto() throws Exception {
        var books = bookService.findAll();

        List<BookDto> ret = books.stream()
                .map(s -> {
                    System.out.println("*********buscando " + s.getId() );

                    AuthorsCliente author = proxyAuthor.findById(s.getId().longValue());
                    return new BookDto(
                            s.getId(),
                            s.getIsbn(),
                            s.getTitle(),
                            s.getAuthor(),
                            s.getPrice(),
                            String.format("%s, %s", author.getLastName(), author.getFirtName())
                    );
                })
                .collect(Collectors.toList());
        return ret;
    }
    @POST
    @Transactional()
    @Operation(summary = "Recibe un objeto",
            description = "Param Book, el cual sera creado en la BDD")
    @APIResponse(responseCode = "404", description = "Book no ingresado")
    @RequestBody(
            name = "book",
            description = "Recibe un objeto Book",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)
            )
    )
    public void insert(Book book) {
        bookService.insert(book);
    }
    @PUT
    @Path("/{id}")
    @Operation(summary = "Recibe un objeto",
            description = "Param Book, el cual sera actualizado en la BDD")
    @APIResponse(responseCode = "404", description = "Book no actualizado")
    public void update(Book book, @PathParam("id") Integer id) {
        book.setId(id);
        bookService.update(book);
    }
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Recibe un objeto",
            description = "Param Book, el cual sera eliminado de la BDD")
    @APIResponse(responseCode = "404", description = "Book no eliminado")
    public void delete( @PathParam("id") Integer id ) {
        bookService.delete(id);
    }
}
