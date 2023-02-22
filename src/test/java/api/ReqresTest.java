package api;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String URL ="https://reqres.in/";
    @Test
    public void checkAvatar(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecOk200());
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data",UserData.class);

       /* users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));*/

        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());
        for(int i = 0; i<avatars.size(); i++){
           Assert.assertTrue(avatars.get(i).contains(ids.get(i)));

        }
    }

    @Test
    public void succesRegisterTest(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecOk200());
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in","pistol");
        SuccsesReg succsesReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccsesReg.class);
        Assert.assertNotNull(succsesReg.getId());
        Assert.assertNotNull(succsesReg.getToken());
        Assert.assertEquals(id, succsesReg.getId());
        Assert.assertEquals(token, succsesReg.getToken());
    }

    @Test
    public void unSuccesUserRegisterTest(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecError400());
        Register user = new Register("sydney@fife","");
        UnSuccesReg unSuccesReg = given()
                .body(user)
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccesReg.class);

        Assert.assertEquals("Missing password",unSuccesReg.getError());

    }

    @Test
    public void sortedYearsTest(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecOk200());
        List<ColorsData> colors =given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
        List<Integer> sortYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(sortYears, years);
        System.out.println(years);
        System.out.println(sortYears);

    }
    @Test
    public void deleteUserTest(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecUniq(204));
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();

    }

    @Test
    public void timeTest(){
        Specifications.instalSpecifications(Specifications.requestSpec(URL),Specifications.responseSpecOk200());
        UserTime user = new UserTime("morpheus","zion resident");
        UserTimeResponse response = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserTimeResponse.class);
        String regex ="(.{5})$";
        String currentTime = Clock.systemUTC().instant().toString().replaceAll(regex,"");
        Assert.assertEquals(currentTime, response.getUpdatedAt().replaceAll(regex,""));
    }
}
