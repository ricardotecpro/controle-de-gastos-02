package br.com.controledegastos.controller;

import br.com.controledegastos.model.Lancamento;
import br.com.controledegastos.repository.LancamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/lancamentos")
@Tag(name = "Lancamentos", description = "CRUD de lancamentos financeiros")
public class LancamentoRestController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @GetMapping
    @Operation(summary = "Listar todos os lancamentos", description = "Retorna a lista completa ordenada por data decrescente")
    public ResponseEntity<List<Lancamento>> listar() {
        List<Lancamento> lancamentos = lancamentoRepository.findAll();
        lancamentos.sort(Comparator.comparing(Lancamento::getData).reversed());
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar lancamento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lancamento encontrado"),
        @ApiResponse(responseCode = "404", description = "Lancamento nao encontrado")
    })
    public ResponseEntity<Lancamento> buscarPorId(
            @Parameter(description = "ID do lancamento") @PathVariable Long id) {
        return lancamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo lancamento")
    @ApiResponse(responseCode = "201", description = "Lancamento criado com sucesso")
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento) {
        Lancamento salvo = lancamentoRepository.save(lancamento);
        return ResponseEntity.created(URI.create("/api/v1/lancamentos/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar lancamento existente")
    public ResponseEntity<Lancamento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Lancamento lancamentoAtualizado) {
        Optional<Lancamento> lancamentoOpt = lancamentoRepository.findById(id);
        if (lancamentoOpt.isPresent()) {
            Lancamento existente = lancamentoOpt.get();
            existente.setDescricao(lancamentoAtualizado.getDescricao());
            existente.setValor(lancamentoAtualizado.getValor());
            existente.setTipo(lancamentoAtualizado.getTipo());
            existente.setData(lancamentoAtualizado.getData());
            return ResponseEntity.ok(lancamentoRepository.save(existente));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir lancamento")
    @ApiResponse(responseCode = "204", description = "Lancamento excluido com sucesso")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (lancamentoRepository.existsById(id)) {
            lancamentoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
