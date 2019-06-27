package support.lfp.requestchain;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/6/26 15:39
 * </pre>
 */
public class tEST {

    public static final void main(String... arg) {


        boolean is = true;

        C c = new C();
        c
                .funtino(is ? new B() : new A())
                .funtino(new B());

    }


    static class A {}

    static class B {}

    static class C {
        final C funtino(A a) {
            return this;
        }

        final C funtino(B a) {
            return this;
        }

        public C funtino(Object o) {
            return null;
        }
    }

}

