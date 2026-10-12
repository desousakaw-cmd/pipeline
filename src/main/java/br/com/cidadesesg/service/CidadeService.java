package br.com.cidadesesg.service;

import br.com.cidadesesg.model.Cidade;
import br.com.cidadesesg.repository.CidadeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CidadeService {
    private final CidadeRepository repository;

    public CidadeService(CidadeRepository repository) {
        this.repository = repository;
    }

    public List<Cidade> listar(String estado) {
        return (estado == null || estado.isBlank())
                ? repository.findAll()
                : repository.findByEstadoIgnoreCase(estado);
    }

    public Cidade buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cidade não encontrada"));
    }

    public Cidade criar(Cidade cidade) {
        cidade.setNome(cidade.getNome().trim());
        cidade.setEstado(cidade.getEstado().trim());
        return repository.save(cidade);
    }

    public Cidade atualizar(Long id, Cidade dados) {
        Cidade atual = buscar(id);
        atual.setNome(dados.getNome().trim());
        atual.setEstado(dados.getEstado().trim());
        atual.setIndiceAmbiental(dados.getIndiceAmbiental());
        atual.setIndiceSocial(dados.getIndiceSocial());
        atual.setIndiceGovernanca(dados.getIndiceGovernanca());
        atual.setPercentualEnergiaRenovavel(dados.getPercentualEnergiaRenovavel());
        return repository.save(atual);
    }

    public void excluir(Long id) {
        Cidade cidade = buscar(id);
        repository.delete(cidade);
    }
}
