package jatymon.babelprojects.abdquorum.data;

public final class Tag {
    private final int version;
    private int seqNumb;

    public Tag(int seqNumb, int version) {
        this.seqNumb = seqNumb;
        this.version = version;
    }

    public boolean isGreater(final Tag other) {
        return seqNumb > other.seqNumb || (seqNumb == other.seqNumb && version > other.version);
    }

    public void setSeqNumb(final int seqNumb) {
        this.seqNumb = seqNumb;
    }

    public int getSeqNumb() {
        return seqNumb;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Tag{seqNumb: %d, version: %d}".formatted(seqNumb, version);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag other && this.seqNumb == other.seqNumb && this.version == other.version;
    }
}
