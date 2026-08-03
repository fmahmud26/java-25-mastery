package shortener.adapters;

import shortener.domain.CodeGenerator;

import java.util.concurrent.atomic.AtomicLong;

/** Demo allocator: base62 of a monotonic counter (replace with Snowflake in prod). */
public final class SequenceCodeGenerator implements CodeGenerator {
    private static final char[] B62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final AtomicLong seq;

    public SequenceCodeGenerator(long start) {
        this.seq = new AtomicLong(start);
    }

    @Override
    public String nextCode() {
        return encode(seq.getAndIncrement());
    }

    static String encode(long n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        long x = n;
        while (x > 0) {
            sb.append(B62[(int) (x % 62)]);
            x /= 62;
        }
        return sb.reverse().toString();
    }
}
