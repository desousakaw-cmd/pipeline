package br.com.cidadesesg.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cidades")
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String estado;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer indiceAmbiental;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer indiceSocial;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer indiceGovernanca;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer percentualEnergiaRenovavel;

    protected Cidade() {}

    public Cidade(String nome, String estado, Integer indiceAmbiental, Integer indiceSocial,
                  Integer indiceGovernanca, Integer percentualEnergiaRenovavel) {
        this.nome = nome;
        this.estado = estado;
        this.indiceAmbiental = indiceAmbiental;
        this.indiceSocial = indiceSocial;
        this.indiceGovernanca = indiceGovernanca;
        this.percentualEnergiaRenovavel = percentualEnergiaRenovavel;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEstado() { return estado; }
    public Integer getIndiceAmbiental() { return indiceAmbiental; }
    public Integer getIndiceSocial() { return indiceSocial; }
    public Integer getIndiceGovernanca() { return indiceGovernanca; }
    public Integer getPercentualEnergiaRenovavel() { return percentualEnergiaRenovavel; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setIndiceAmbiental(Integer indiceAmbiental) { this.indiceAmbiental = indiceAmbiental; }
    public void setIndiceSocial(Integer indiceSocial) { this.indiceSocial = indiceSocial; }
    public void setIndiceGovernanca(Integer indiceGovernanca) { this.indiceGovernanca = indiceGovernanca; }
    public void setPercentualEnergiaRenovavel(Integer percentualEnergiaRenovavel) { this.percentualEnergiaRenovavel = percentualEnergiaRenovavel; }
}
