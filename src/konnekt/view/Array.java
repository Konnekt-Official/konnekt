import  java.util.Scanner;

public class Array {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number:");
        int num = sc.nextInt();

        int[] arr = new int[num];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;               // storing value
            System.out.println(arr[i]); // printing value
        }
    }
}
