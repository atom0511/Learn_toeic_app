package trungatom.tqt.secondapp.models;

public class WordModel {
    private int id;
    String origin;
    String explanation;
    String type;
    String pronunciation;
    String imageURL;
    String example;
    String example_trans;
    int topic_id;
    int level;

    public WordModel(int id, String origin, String explanimation, String type,
                     String pronunciantion, String imageURL, String example, String example_trans, int topic_id, int level) {
        this.id = id;
        this.origin = origin;
        this.explanation = explanimation;
        this.type = type;
        this.pronunciation = pronunciantion;
        this.imageURL = imageURL;
        this.example = example;
        this.example_trans = example_trans;
        this.topic_id = topic_id;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPronunciantion() {
        return pronunciation;
    }

    public void setPronunciantion(String pronunciantion) {
        this.pronunciation = pronunciantion;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExample_trans() {
        return example_trans;
    }

    public void setExample_trans(String example_trans) {
        this.example_trans = example_trans;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
