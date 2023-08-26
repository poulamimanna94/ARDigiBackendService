package com.siemens.dto;

import java.time.Instant;

/**
 * A Document.
 */
public class DocumentDTO
{
    
    private Long id;
    private String docName;
    private String docLink;
    private Instant createdAt;
    private Instant modifiedAt;
    
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
    
    public Instant getCreatedAt()
    {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt = createdAt;
    }
    
    public Instant getModifiedAt()
    {
        return modifiedAt;
    }
    
    public void setModifiedAt(Instant modifiedAt)
    {
        this.modifiedAt = modifiedAt;
    }
    
    // ctor
    public DocumentDTO()
    {
        super();
    }
    
    public DocumentDTO(Long id, String docName, String docLink, Instant createdAt, Instant modifiedAt)
    {
        super();
        this.id = id;
        this.docName = docName;
        this.docLink = docLink;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    
    @Override
    public String toString()
    {
        return "Document [id=" + id +
            ", docName=" + docName +
            ", docLink=" + docLink +
            ", createdAt=" + createdAt +
            ", modifiedAt=" + modifiedAt + "]";
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof DocumentDTO))
        {
            return false;
        }
        return id != null && id.equals(((DocumentDTO) o).id);
    }
    
    @Override
    public int hashCode()
    {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
    
}
