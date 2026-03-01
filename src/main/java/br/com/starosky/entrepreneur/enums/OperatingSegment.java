package br.com.starosky.entrepreneur.enums;

public enum OperatingSegment {
    TECHNOLOGY("technology"),
    COMMERCE("commerce"),
    INDUSTRY("industry"),
    SERVICES("services"),
    AGRIBUSINESS("agribusiness");

    private final String value;

    OperatingSegment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OperatingSegment fromValue(String value) {
        for (OperatingSegment segment : OperatingSegment.values()) {
            if (segment.value.equalsIgnoreCase(value)) {
                return segment;
            }
        }
        throw new IllegalArgumentException("Segmento de atuação inválido: " + value);
    }
}
