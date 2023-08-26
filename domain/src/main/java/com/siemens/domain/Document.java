package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Document extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_sequenceGenerator")
    @SequenceGenerator(name = "doc_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "doc_name")
    private String docName;
    
    @Column(name = "doc_link")
    private String docLink;
    
    // @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    // @JsonIgnoreProperties(value = { "assets", "unit" }, allowSetters = true)
    @JsonIgnore
    private Asset asset;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getDocName()
    {
        return docName;
    }
    
    public void setDocName(String docName)
    {
        this.docName = docName;
    }
    
    public String getDocLink()
    {
        return docLink;
    }
    
    public void setDocLink(String docLink)
    {
        this.docLink = docLink;
    }
    
    public Asset getAsset()
    {
        return this.asset;
    }
    
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }
    
    public Document()
    {
        super();
    }
    
    public Document(Long id, String docName, String docLink, Instant createdAt, Instant modifiedAt)
    {
        super();
        this.id = id;
        this.docName = docName;
        this.docLink = docLink;
    }
    
    @Override
    public String toString()
    {
        return "Document [id=" + id +
            ", docName=" + docName +
            ", docLink=" + docLink + "]";
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Document))
        {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }
    
    @Override
    public int hashCode()
    {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
    
}
