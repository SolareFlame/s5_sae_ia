public class MainTestFonctionActivation {
    public static void main(String[] args) {
        var sigmoide = new Sigmoide();
        System.out.println("###################################################");
        System.out.println("                Test Sigmoide");
        System.out.println("---------------------------------------------------");
        System.out.println("Entree      Evaluate             EvaluateDer");
        System.out.println("-1.0   |    "+sigmoide.evaluate(-1.0)+" | "+ sigmoide.evaluateDer(sigmoide.evaluate(-1.0)));
        System.out.println("-0.5   |    "+sigmoide.evaluate(-0.5)+" | "+ sigmoide.evaluateDer(sigmoide.evaluate(-0.5)));
        System.out.println("0      |    "+sigmoide.evaluate(0)+"                | "+ sigmoide.evaluateDer(sigmoide.evaluate(0)));
        System.out.println("0.5    |    "+sigmoide.evaluate(0.5)+" | "+ sigmoide.evaluateDer(sigmoide.evaluate(0.5)));
        System.out.println("1.0    |    "+sigmoide.evaluate(1.0)+" | "+ sigmoide.evaluateDer(sigmoide.evaluate(1.0)));
        var tanjente = new TangenteHyperbolique();
        System.out.println("###################################################");
        System.out.println("             Test Tangente Hyperbolique");
        System.out.println("---------------------------------------------------");
        System.out.println("Entree     Evaluate               EvaluateDer");
        System.out.println("-1.0   |   "+tanjente.evaluate(-1.0)+"  | "+ tanjente.evaluateDer(tanjente.evaluate(-1.0)));
        System.out.println("-0.5   |   "+tanjente.evaluate(-0.5)+" | "+ tanjente.evaluateDer(tanjente.evaluate(-0.5)));
        System.out.println("0      |   "+tanjente.evaluate(0)+"                  | "+ tanjente.evaluateDer(tanjente.evaluate(0)));
        System.out.println("0.5    |   "+tanjente.evaluate(0.5)+"  | "+ tanjente.evaluateDer(tanjente.evaluate(0.5)));
        System.out.println("1.0    |   "+tanjente.evaluate(1.0)+"   | "+ tanjente.evaluateDer(tanjente.evaluate(1.0)));
        System.out.println("###################################################");
    }
}
