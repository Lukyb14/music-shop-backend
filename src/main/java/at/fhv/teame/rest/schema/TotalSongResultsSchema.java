package at.fhv.teame.rest.schema;

import javax.xml.bind.annotation.XmlElement;

public class TotalSongResultsSchema {
    @XmlElement
    public Integer results;

    public TotalSongResultsSchema() {
        this.results = 0;
    }

    public TotalSongResultsSchema(Integer results) {
        this.results = results;
    }
}
