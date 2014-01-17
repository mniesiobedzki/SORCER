package sorcer.trigo.junit;

import static sorcer.eo.operator.cxt;
import static sorcer.eo.operator.exert;
import static sorcer.eo.operator.in;
import static sorcer.eo.operator.sig;
import static sorcer.eo.operator.srv;
import static sorcer.eo.operator.task;
import static sorcer.eo.operator.value;

import java.rmi.RMISecurityManager;
import java.util.logging.Logger;

import org.junit.Test;

import sorcer.core.SorcerConstants;
import sorcer.core.exertion.ObjectJob;
import sorcer.trigo.provider.Cos;
import sorcer.trigo.provider.Max;
import sorcer.trigo.provider.Sin;
import sorcer.service.Exertion;
import sorcer.service.Job;
import sorcer.service.ServiceExertion;
import sorcer.service.Task;

import sorcer.util.Sorcer;

//@SuppressWarnings("unchecked")
public class TrigoTest implements SorcerConstants {

    private final static Logger logger = Logger.getLogger(TrigoTest.class.getName());

    static {
        ServiceExertion.debug = true;
        System.setProperty("java.security.policy", Sorcer.getHome()
                + "/configs/policy.all");
        System.setSecurityManager(new RMISecurityManager());
        Sorcer.setCodeBase(new String[]{"SinProvider.jar", "CosProvider.jar",  "MaxProvider.jar"});
        System.out.println("CLASSPATH :" + System.getProperty("java.class.path"));
        System.setProperty("java.protocol.handler.pkgs", "sorcer.util.url|org.rioproject.url");
    }

    @Test
    public void maxOfSinAndCos() throws Exception {

        double sinInput = 1;
        double cosInput = 2;

        Task sin = srv("sin", sig("sin", Sin.class), cxt("sin", in("input", sinInput)));
        Task cos = srv("cos", sig("cos", Cos.class), cxt("cos", in("input", cosInput)));
        Task max = srv("max", sig("max", Max.class), cxt("max", in("a"), in("b")));

        Exertion job = new ObjectJob("Job");
        job.addExertion(sin);
        job.addExertion(cos);
        job.addExertion(max);

        max.getContext().connect("sin", "value", sin.getContext());
        max.getContext().connect("cos", "value", cos.getContext());

        job = job.exert();

        logger.info("job context: " + ((Job) job).getJobContext());
    }


}