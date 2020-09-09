package advisor.processing;

import java.net.http.HttpResponse;

public interface Processing {

    public void process(HttpResponse<String> response);

}
