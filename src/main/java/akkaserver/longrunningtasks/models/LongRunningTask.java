package akkaserver.longrunningtasks.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public class LongRunningTask {

    @JsonProperty("largeOutput")
    private BigInteger largeOutput;

    public LongRunningTask(BigInteger largeOutput) {
        this.largeOutput = largeOutput;
    }

    public BigInteger getLargeOutput() {
        return largeOutput;
    }

    public void setLargeOutput(BigInteger largeOutput) {
        this.largeOutput = largeOutput;
    }
}
