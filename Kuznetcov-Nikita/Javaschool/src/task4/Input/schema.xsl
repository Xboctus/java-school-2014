<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema">

    <xsd:element name="catalog">

        <xsd:complexType>

            <xsd:sequence>

                <xsd:element name="book"

                             minOccurs="0"

                             maxOccurs="unbounded">

                    <xsd:complexType>

                        <xsd:sequence>

                            <xsd:element name="author" type="xsd:string"/>

                            <xsd:element name="title" type="xsd:string"/>

                            <xsd:element name="genre" type="xsd:string"/>

                            <xsd:element name="isbn" type="xsd:string"
                                         minOccurs="0"

                                         maxOccurs="1"/>

                            <xsd:element name="price" type="xsd:float"/>

                            <xsd:element name="publish_date" type="xsd:date"/>

                            <xsd:element name="description" type="xsd:string"/>

                        </xsd:sequence>

                        <xsd:attribute name="id" type="xsd:string"/>

                    </xsd:complexType>

                </xsd:element>

            </xsd:sequence>

        </xsd:complexType>

    </xsd:element>

</xsd:schema>
