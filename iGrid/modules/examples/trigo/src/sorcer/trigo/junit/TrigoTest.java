package sorcer.trigo.junit;

import java.rmi.RMISecurityManager;
import java.util.logging.Logger;

import org.junit.Test;

import sorcer.service.*;
import sorcer.service.Strategy.Flow;

import sorcer.core.SorcerConstants;
import sorcer.core.exertion.ObjectJob;
import sorcer.trigo.provider.Cos;
import sorcer.trigo.provider.Max;
import sorcer.trigo.provider.Sin;

import sorcer.util.Sorcer;

import static sorcer.eo.operator.*;
import static sorcer.eo.operator.input;
import static sorcer.eo.operator.out;

//@SuppressWarnings("unchecked")
public class TrigoTest implements SorcerConstants {

    private final static Logger logger = Logger.getLogger(TrigoTest.class.getName());

    static {
        ServiceExertion.debug = true;
        System.setProperty("java.security.policy", Sorcer.getHome()
                + "/configs/policy.all");
        System.setSecurityManager(new RMISecurityManager());
        Sorcer.setCodeBase(new String[]{"SinProvider.jar", "CosProvider.jar", "MaxProvider.jar"});
        System.out.println("CLASSPATH :" + System.getProperty("java.class.path"));
        System.setProperty("java.protocol.handler.pkgs", "sorcer.util.url|org.rioproject.url");
    }

    @Test
    public void sinTest() throws Exception {
        Task t4 = task("sin",
                sig("Sin", Sin.class),
                context("sin", in("sin", 1)));

        t4 = exert(t4);
        System.out.println("tin" + value(t4));
    }

    @Test
    public void connectMaxOfSinAndCos() throws Exception {

        double sinInput = 0.0;
        double cosInput = 0.0;

        Task sin = srv("sin", sig("sin", Sin.class), cxt("sin", in("input", sinInput)));
        Task cos = srv("cos", sig("cos", Cos.class), cxt("cos", in("input", cosInput)));
        Task max = srv("max", sig("max", Max.class), cxt("max", in("a"), in("b")));

        Exertion job = new ObjectJob("Max of Cos and Sin using connect()");

        job.addExertion(cos);
        job.addExertion(sin);
        job.addExertion(max);

        sin.getContext().connect("value", "a", max.getContext());
        cos.getContext().connect("value", "b", max.getContext());


        job = job.exert();

        logger.info("Max: " + value(max,"value"));
    }

    @Test
    public void pipeMaxOfSinAndCos() throws Exception {

        double sinInput = 0.0;
        double cosInput = 0.0;

        Task sin = srv("sin", sig("sin", Sin.class), cxt("sin", in("input", sinInput)));
        Task cos = srv("cos", sig("cos", Cos.class), cxt("cos", in("input", cosInput)));
        Task max = srv("max", sig("max", Max.class), cxt("max", in("sin"), in("cos")));

        Job job = job("max",
                job("trigo", sin, cos, strategy(Flow.PAR, Strategy.Access.PUSH)),
                strategy(Flow.SEQ, Strategy.Access.PUSH),
                max,
                pipe(out(sin, "value"), input(max, "sin")),
                pipe(out(cos, "value"), input(max, "cos")));

        logger.info("Max: " + value(job,"max/max/value"));
    }

    @Test
    public void max() throws Exception {
        Task max = task(
                "max",
                sig("max", Max.class),
                context(
                        "max",
                        in("x", -10.0),
                        in("y", 0.0),
                        in("z", 10.0
                        )
                )
        );

        logger.info("Max: " + value(max, "value"));
    }

    @Test
    public void sin() throws Exception {
        Task sin = task(
                "sin",
                sig("sin", Sin.class),
                context(
                        "sin",
                        in("input", Math.toRadians(90))
                )
        );

        logger.info("Sin: " + value(sin, "value"));
    }


    @Test
    public void cos() throws Exception {
        Task cos = task(
                "cos",
                sig("cos", Cos.class),
                context(
                        "cos",
                        in("input", 0.0)
                )
        );

        logger.info("Cos: " + value(cos, "value"));
    }


}