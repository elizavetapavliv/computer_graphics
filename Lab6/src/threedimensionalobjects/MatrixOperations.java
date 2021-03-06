package threedimensionalobjects;

public class MatrixOperations {

    public static double[][] multiplyMatrixes(double[][] a, double[][] b) {
        double[][] c = new double[a.length][4];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }
    public static float[] multiplyVectorMatrix(float[][] a, float[] b) {
        float[] c = new float[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                c[i] += b[j] * a[j][i];   
            }
        }
        return c;
    }
}
