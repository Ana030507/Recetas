package co.edu.usco.springBoot_securityMemory.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.edu.usco.springBoot_securityMemory.model.Receta;
import co.edu.usco.springBoot_securityMemory.repository.RecetaRepository;

@Controller
@RequestMapping
public class RecetaController {

    @Autowired
    private RecetaRepository recetaRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // Mostrar todas las recetas (vista para el usuario)
    @GetMapping("/user/hello")
    public String mostrarRecetas(Model model) {
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        return "user-hello";
    }

    // Vista del admin con todas las recetas
    @GetMapping("/admin/hello")
    public String adminRecetas(Model model) {
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        return "admin-hello";
    }

    // Vista del chef (muestra todas las recetas y el username)
    @GetMapping("/chef/hello")
    public String showChefPanel(Model model, @AuthenticationPrincipal User user) {
        List<Receta> recetas = recetaRepository.findAll();
        model.addAttribute("recetas", recetas);
        model.addAttribute("username", user != null ? user.getUsername() : "");
        return "chef-hello";
    }

    /**
     * Endpoint único para agregar recetas.
     * - Lo usamos para admin y para chef.
     * - Guarda autor = nombre del usuario autenticado.
     * - Después redirige dinámicamente según el rol del que hizo la petición.
     */
    @PostMapping("/admin/agregar")
    public String agregarReceta(@RequestParam("titulo") String titulo,
                                @RequestParam("descripcion") String descripcion,
                                @RequestParam("ingredientes") String ingredientes,
                                @RequestParam("pasos") String pasos,
                                @RequestParam("imagen") MultipartFile imagen,
                                Authentication authentication)
                                {

        Receta receta = new Receta();
        receta.setTitulo(titulo);
        receta.setDescripcion(descripcion);
        receta.setIngredientes(ingredientes);
        receta.setPasos(pasos);

        // guardar el autor (usuario autenticado)
        String username = (authentication != null) ? authentication.getName() : "unknown";
        receta.setAutor(username);

     // Guardar la imagen si se sube una 
        if (!imagen.isEmpty()) 
        { try 
        	{ byte[] bytes = imagen.getBytes();
        	Path path = Paths.get(UPLOAD_DIR + 
        imagen.getOriginalFilename());
        	Files.write(path, bytes); 
        	receta.setImagenUrl("/uploads/" + 
        imagen.getOriginalFilename()); 
        	} catch (IOException e) { 
        		e.printStackTrace(); 
        	}
        }

        recetaRepository.save(receta);

        // redirigir según rol del usuario que hizo la petición
        boolean isChef = false;
        if (authentication != null) {
            isChef = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CHEF"));
        }

        if (isChef) {
            return "redirect:/chef/hello";
        } else {
            return "redirect:/admin/hello";
        }
    }

    // Eliminar receta (solo admin, controlado por SecurityConfig)
    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReceta(@PathVariable("id") Long id) {
        recetaRepository.deleteById(id);
        return "redirect:/admin/hello";
    }

    // Obtener receta por ID (usado para editar — devuelve JSON)
    @GetMapping("/admin/editar/{id}")
    @ResponseBody
    public Receta obtenerReceta(@PathVariable("id") Long id) {
        return recetaRepository.findById(id).orElse(null);
    }

    // Actualizar una receta existente (solo admin)
    @PostMapping("/admin/actualizar")
    public String actualizarReceta(@RequestParam("id") Long id,
                                    @RequestParam("titulo") String titulo,
                                    @RequestParam("descripcion") String descripcion,
                                    @RequestParam("ingredientes") String ingredientes,
                                    @RequestParam("pasos") String pasos,
                                    @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        Receta receta = recetaRepository.findById(id).orElse(null);
        if (receta != null) {
            receta.setTitulo(titulo);
            receta.setDescripcion(descripcion);
            receta.setIngredientes(ingredientes);
            receta.setPasos(pasos);

            if (imagen != null && !imagen.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
                    Path path = Paths.get(UPLOAD_DIR + fileName);
                    Files.createDirectories(path.getParent());
                    Files.write(path, imagen.getBytes());
                    receta.setImagenUrl("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            recetaRepository.save(receta);
        }

        return "redirect:/admin/hello";
    }
    
}
