package org.ds.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import org.ds.proto.Book;
import org.ds.proto.BookCollection;

import java.nio.charset.StandardCharsets;

public class BookMain {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        byte[] protoBytes = sender();
        receiver(protoBytes);
        compareWithJson(protoBytes);
    }

    private static byte[] sender() {
        Book.Publisher pub1 = Book.Publisher.newBuilder()
                .setName("Acme Publishing")
                .setAddress("123 Main St")
                .build();

        Book book1 = Book.newBuilder()
                .setTitle("Effective Examples")
                .setAuthor("Mohamad")
                .setIsbn("978-1-23456-789-7")
                .setYear(2020)
                .setGenre(Book.Genre.SCIENCE)
                .addTags("programming")
                .addTags("protobuf")
                .setPublisher(pub1)
                .build();

        Book.Publisher pub2 = Book.Publisher.newBuilder()
                .setName("BooksCo")
                .setAddress("456 Side Ave")
                .build();

        Book book2 = Book.newBuilder()
                .setTitle("Practical Patterns")
                .setAuthor("Ahmad")
                .setIsbn("978-0-98765-432-1")
                .setYear(2018)
                .setGenre(Book.Genre.FICTION)
                .addTags("design")
                .setPublisher(pub2)
                .build();

        BookCollection collection = BookCollection.newBuilder()
                .addBooks(book1)
                .addBooks(book2)
                .build();

        byte[] bytes = collection.toByteArray();

        System.out.println("Protobuf bytes:");
        for (byte b : bytes) System.out.print((b & 0xFF) + " ");
        System.out.println("\nProtobuf size: " + bytes.length + " bytes");
        return bytes;
    }

    private static void receiver(byte[] msg) throws InvalidProtocolBufferException {
        BookCollection collection = BookCollection.parseFrom(msg);
        System.out.println("Decoded BookCollection:");
        System.out.println(collection);
    }

    private static void compareWithJson(byte[] protoBytes) {
        // Build equivalent JSON manually
        String json = "[" +
                "{\"title\":\"Effective Examples\",\"author\":\"Mohamad\",\"isbn\":\"978-1-23456-789-7\",\"year\":2020,\"genre\":\"SCIENCE\",\"tags\":[\"programming\",\"protobuf\"],\"publisher\":{\"name\":\"Acme Publishing\",\"address\":\"123 Main St\"}}," +
                "{\"title\":\"Practical Patterns\",\"author\":\"Ahmad\",\"isbn\":\"978-0-98765-432-1\",\"year\":2018,\"genre\":\"FICTION\",\"tags\":[\"design\"],\"publisher\":{\"name\":\"BooksCo\",\"address\":\"456 Side Ave\"}}" +
                "]";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        System.out.println("JSON string:");
        System.out.println(json);
        System.out.println("JSON size (UTF-8): " + jsonBytes.length + " bytes");
        System.out.println("Protobuf size: " + protoBytes.length + " bytes");
    }
}
