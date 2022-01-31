interface LType {

    String show();

    boolean equals(LType o);

    String getJVMTypeName();

    String getJVMFieldTypeName();
}
