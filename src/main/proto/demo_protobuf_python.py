# demo_protobuf_python.py
import json
from google.protobuf import json_format
import book_pb2

def build_collection():
    pub1 = book_pb2.Book.Publisher(name="Acme Publishing", address="123 Main St")
    book1 = book_pb2.Book(
        title="Effective Examples",
        author="Mohamad",
        isbn="978-1-23456-789-7",
        year=2020,
        genre=book_pb2.Book.SCIENCE,
        publisher=pub1
    )
    book1.tags.extend(["programming", "protobuf"])

    pub2 = book_pb2.Book.Publisher(name="BooksCo", address="456 Side Ave")
    book2 = book_pb2.Book(
        title="Practical Patterns",
        author="Ahmad",
        isbn="978-0-98765-432-1",
        year=2018,
        genre=book_pb2.Book.FICTION,
        publisher=pub2
    )
    book2.tags.append("design")

    collection = book_pb2.BookCollection()
    collection.books.extend([book1, book2])
    return collection

def main():
    # Build and serialize (Protobuf)
    collection = build_collection()
    proto_bytes = collection.SerializeToString()

    print("Protobuf bytes (as unsigned ints):")
    print([b for b in proto_bytes])
    print("Protobuf size:", len(proto_bytes), "bytes\n")

    # Deserialize
    parsed = book_pb2.BookCollection()
    parsed.ParseFromString(proto_bytes)
    print("Decoded BookCollection (protobuf -> text):")
    print(parsed)               # readable text representation
    print()

    # Convert to JSON (two ways)
    # 1) Using google.protobuf.json_format (best)
    json_text = json_format.MessageToJson(collection, preserving_proto_field_name=True)
    json_bytes = json_text.encode("utf-8")
    print("JSON (from protobuf Message):")
    print(json_text)
    print("JSON size (UTF-8):", len(json_bytes), "bytes")
    print("Protobuf size:", len(proto_bytes), "bytes\n")

    # 2) Manual native JSON (for exact control)
    native = []
    for b in collection.books:
        native.append({
            "title": b.title,
            "author": b.author,
            "isbn": b.isbn,
            "year": b.year,
            "genre": book_pb2.Book.Genre.Name(b.genre),
            "tags": list(b.tags),
            "publisher": {"name": b.publisher.name, "address": b.publisher.address}
        })
    native_json = json.dumps(native, ensure_ascii=False)
    print("Native JSON size (UTF-8):", len(native_json.encode("utf-8")), "bytes")
    print("Native JSON:", native_json)

if __name__ == "__main__":
    main()
