package advisor;

public class View {
    private static View instance;
    private int currentPage;
    private int pageAmount;
    private String[] data;

    private View() {
    }

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }

    public void setData(String[] data) {
        this.data = data;
        this.currentPage = 1;
        this.pageAmount = data.length % Config.PAGINATION == 0 ? data.length / Config.PAGINATION : data.length / Config.PAGINATION + 1;
    }

    private String getPage(int pageNumber) {
        StringBuilder result = new StringBuilder();
        int lowerLimit = Config.PAGINATION * (currentPage - 1);
        int upperLimit = pageNumber == pageAmount ? data.length - 1 : Config.PAGINATION * pageNumber - 1;

        for (int i = lowerLimit; i <= upperLimit; i++) {
            result.append(data[i]);
        }
        result.append(String.format("---PAGE %d OF %d---", pageNumber, pageAmount));
        return result.toString();
    }

    public String getFirstPage() {
        return getPage(1);
    }

    public String getNextPage() {
        if (currentPage == pageAmount) {
            return "No more pages.";
        } else {
            currentPage = currentPage + 1;
            return getPage(currentPage);
        }
    }

    public String getPreviousPage() {
        if (currentPage == 1) {
            return "No more pages.";
        } else {
            currentPage = currentPage - 1;
            return getPage(currentPage);
        }
    }
}
