package org.slavik.DioritB2B;

import org.slavik.ApiClient;
<<<<<<< Updated upstream
import org.slavik.DioritB2B.model.ShortProduct;
=======
import org.slavik.DioritB2B.model.Datum;
>>>>>>> Stashed changes

import java.io.IOException;
import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<ShortProduct> getAllProduct() throws IOException;
}
