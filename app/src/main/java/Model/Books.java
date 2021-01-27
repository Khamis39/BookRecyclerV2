package Model;

import Model.User;

public class Books {
    private String  name ;
    private String Image;
    private String Description;
    private String Condition;
    private String BookId;
    private String Price;
    private User Owner;

    public Books() {
    }

    public Books(String name, String Image, String description,String Condition, String BookId,String Price,User owner) {
        this.name = name;
        this.Image = Image;
        this.Description = description;
        this.Condition = Condition;
        this.BookId = BookId;
        this.Price=Price;
        this.Owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String BookId) {
        this.BookId = BookId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        Price = Price;
    }

    public User getOwner() {
        return Owner;
    }

    public void setOwner(User owner) {
        Owner = owner;
    }
}
