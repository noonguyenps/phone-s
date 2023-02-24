package project.phoneshop.mservice.processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import project.phoneshop.mservice.config.Environment;
import project.phoneshop.mservice.config.PartnerInfo;
import project.phoneshop.mservice.shared.exception.MoMoException;
import project.phoneshop.mservice.shared.utils.Execute;

/**
 * @author hainguyen
 * Documention: https://developers.momo.vn
 */

public abstract class AbstractProcess<T, V> {

    protected PartnerInfo partnerInfo;
    protected Environment environment;
    protected Execute execute = new Execute();

    public AbstractProcess(Environment environment) {
        this.environment = environment;
        this.partnerInfo = environment.getPartnerInfo();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public abstract V execute(T request) throws MoMoException;
}
