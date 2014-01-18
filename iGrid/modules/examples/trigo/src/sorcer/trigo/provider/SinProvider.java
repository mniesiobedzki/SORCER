package sorcer.trigo.provider;

import static sorcer.eo.operator.revalue;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

import com.sun.jini.start.LifeCycle;

import sorcer.core.context.Contexts;
import sorcer.core.context.PositionalContext;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.service.ContextException;

/**
 * @author Macha
 */
@SuppressWarnings("rawtypes")
public class SinProvider extends ServiceTasker implements sorcer.trigo.provider.Sin {

    public static final String VALUE = "value";

    public SinProvider(String[] args, LifeCycle lifeCycle) throws Exception {
        super(args, lifeCycle);
    }


    @SuppressWarnings("unchecked")
    public Context sin(Context context) throws RemoteException, ContextException {

        PositionalContext cxt = (PositionalContext) context;
        try {

            List<Double> inputs = (List<Double>) Contexts.getNamedInValues(context);
            if (inputs == null || inputs.size() == 0) {
                inputs = (List<Double>) Contexts.getPrefixedInValues(context);
            }
            if (inputs == null || inputs.size() == 0) inputs = (List<Double>) cxt.getInValues();

            double input = (Double) revalue(inputs.get(0));
            cxt.putValue(VALUE, Math.sin(input));

        } catch (Exception ex) {
            ex.printStackTrace();
            context.reportException(ex);
            throw new ContextException(" calculate exception", ex);
        }
        return (Context) context;
    }

}
