package com.revature.services;

import com.revature.models.Reimbursement;
import com.revature.repositories.ReimbursementsRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ReimbursementServiceTest {


    ReimbursementService reimbursementService;
    @Mock
    ReimbursementsRepository reimbursementsRepository;

    @Before
    public void setUpTest(){
        reimbursementsRepository = Mockito.mock(ReimbursementsRepository.class);
        reimbursementService = new ReimbursementService(reimbursementsRepository);
    }

    @After
    public void tearDownTest(){
        reimbursementsRepository = null;
        reimbursementService = null;
    }

    //-----------------------------------------------------------

    @Test
    public void test_getAllReimbursementsWhenEmpty(){
        Mockito.when(reimbursementsRepository.getAllReimbursements()).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getAllReimb());
    }

    @Test
    public void test_getAllReimbursementsWhenEntriesExist(){
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementService.getAllReimb()).thenReturn(reimbursementList);

        Assert.assertEquals(reimbursementList, reimbursementService.getAllReimb());
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByUserId_withIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByUserId(-1));
    }

    @Test
    public void test_getReimbByUserId_whenUserHasNoReimb(){
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorId(1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByUserId(1));
    }

    @Test
    public void test_getReimbByUserId_whenUserHasReimb(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorId(1)).thenReturn(reimbursements);
        Assert.assertEquals(reimbursements, reimbursementService.getReimbByUserId(1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByAuthorAndStatus_whenAuthorIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndStatus(-1, 1));
    }

    @Test
    public void test_getReimbByAuthorAndStatus_whenStatIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndStatus(1, -1));
    }

    @Test
    public void test_getReimbByAuthorAndStatus_whenStatIdGreaterThanFour(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndStatus(1, 5));
    }

    @Test
    public void test_getReimbByAuthorAndStatus_whenListEmpty() throws SQLException {
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorIdAndStatus(1, 1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByAuthorAndStatus(1, 1));
    }

    @Test
    public void test_getReimbByAuthorAndStatus_whenListHasReimb() throws SQLException {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorIdAndStatus(1, 1)).thenReturn(reimbursementList);
        Assert.assertEquals(reimbursementList, reimbursementService.getReimbByAuthorAndStatus(1, 1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByAuthorAndType_whenAuthorIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndType(-1, 1));
    }

    @Test
    public void test_getReimbByAuthorAndType_whenStatIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndType(1, -1));
    }

    @Test
    public void test_getReimbByAuthorAndType_whenStatIdGreaterThanFour(){
        Assert.assertNull(reimbursementService.getReimbByAuthorAndType(1, 5));
    }

    @Test
    public void test_getReimbByAuthorAndType_whenListEmpty() throws SQLException {
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorIdAndType(1, 1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByAuthorAndType(1, 1));
    }

    @Test
    public void test_getReimbByAuthorAndType_whenListHasReimb() throws SQLException {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByAuthorIdAndType(1, 1)).thenReturn(reimbursementList);
        Assert.assertEquals(reimbursementList, reimbursementService.getReimbByAuthorAndType(1, 1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByResolverAndStatus_whenResolverIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndStatus(-1, 1));
    }

    @Test
    public void test_getReimbByResolverAndStatus_whenStatIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndStatus(1, -1));
    }

    @Test
    public void test_getReimbByResolverAndStatus_whenStatIdGreaterThanFour(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndStatus(1, 5));
    }

    @Test
    public void test_getReimbByResolverAndStatus_whenListEmpty() throws SQLException {
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndStatus(1, 1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByResolverAndStatus(1, 1));
    }

    @Test
    public void test_getReimbByResolverAndStatus_whenListHasReimb() throws SQLException {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndStatus(1, 1)).thenReturn(reimbursementList);
        Assert.assertEquals(reimbursementList, reimbursementService.getReimbByResolverAndStatus(1, 1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByResolverAndType_whenResolverIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndType(-1, 1));
    }

    @Test
    public void test_getReimbByResolverAndType_whenStatIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndType(1, -1));
    }

    @Test
    public void test_getReimbByResolverAndType_whenStatIdGreaterThanFour(){
        Assert.assertNull(reimbursementService.getReimbByResolverAndType(1, 5));
    }

    @Test
    public void test_getReimbByResolverAndType_whenListEmpty() throws SQLException {
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndType(1, 1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByResolverAndType(1, 1));
    }

    @Test
    public void test_getReimbByResolverAndType_whenListHasReimb() throws SQLException {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndType(1, 1)).thenReturn(reimbursementList);
        Assert.assertEquals(reimbursementList, reimbursementService.getReimbByResolverAndType(1, 1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByReimbId_withIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByReimbId(-1));
    }

    @Test
    public void test_getReimbById_whenNoReimb() throws SQLException {
        Optional<Reimbursement> o = Optional.empty();
        Mockito.when(reimbursementsRepository.getAReimbByReimbId(1)).thenReturn(o);
        Assert.assertNull(reimbursementService.getReimbByReimbId(1));
    }

    @Test
    public void test_getReimbByReimbId_whenYesReimb() throws SQLException {
        Reimbursement r = new Reimbursement();
        Optional<Reimbursement> o = Optional.of(r);
        Mockito.when(reimbursementsRepository.getAReimbByReimbId(1)).thenReturn(o);
        Assert.assertEquals(r, reimbursementService.getReimbByReimbId(1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByReimbIdOverload_withRequesterIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByReimbId(-1, 1));
    }

    @Test
    public void test_getReimbByReimbIdOverload_withRembIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByReimbId(1, -1));
    }

    @Test
    public void test_getReimbByIdOverload_whenNoReimb() throws SQLException {
        Optional<Reimbursement> o = Optional.empty();
        Mockito.when(reimbursementsRepository.getAReimbByReimbId(1)).thenReturn(o);
        Assert.assertNull(reimbursementService.getReimbByReimbId(1, 1));
    }

    @Test
    public void test_getReimbByReimbIdOverload_whenYesReimbNoEqual() throws SQLException {
        Reimbursement r = new Reimbursement();
        r.setAuthorId(2);
        Optional<Reimbursement> o = Optional.of(r);
        Mockito.when(reimbursementsRepository.getAReimbByReimbId(1)).thenReturn(o);
        Assert.assertNull(reimbursementService.getReimbByReimbId(1, 1));
    }

    @Test
    public void test_getReimbByReimbIdOverload_whenYesReimbYesEqual() throws SQLException {
        Reimbursement r = new Reimbursement();
        r.setAuthorId(1);
        Optional<Reimbursement> o = Optional.of(r);
        Mockito.when(reimbursementsRepository.getAReimbByReimbId(1)).thenReturn(o);
        Assert.assertEquals(r, reimbursementService.getReimbByReimbId(1, 1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByType_withTypeLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByType(-1));
    }

    @Test
    public void test_getReimbByType_withTypeGreaterThanFour(){
        Assert.assertNull(reimbursementService.getReimbByType(5));
    }

    @Test
    public void test_getReimbByType_whenUserHasNoReimb(){
        Mockito.when(reimbursementsRepository.getAllReimbSetByType(1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByType(1));
    }

    @Test
    public void test_getReimbByType_whenUserHasReimb(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByType(1)).thenReturn(reimbursements);
        Assert.assertEquals(reimbursements, reimbursementService.getReimbByType(1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByStatus_withTypeLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByType(-1));
    }

    @Test
    public void test_getReimbByStatus_withTypeGreaterThanThree(){
        Assert.assertNull(reimbursementService.getReimbByType(5));
    }

    @Test
    public void test_getReimbByStatus_whenUserHasNoReimb(){
        Mockito.when(reimbursementsRepository.getAllReimbSetByStatus(1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByStatus(1));
    }

    @Test
    public void test_getReimbByStatus_whenUserHasReimb(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByStatus(1)).thenReturn(reimbursements);
        Assert.assertEquals(reimbursements, reimbursementService.getReimbByStatus(1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_getReimbByResolverId_withIdLessThanZero(){
        Assert.assertNull(reimbursementService.getReimbByResolver(-1));
    }

    @Test
    public void test_getReimbByResolverId_whenUserHasNoReimb() throws SQLException {
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverId(1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByResolver(1));
    }

    @Test
    public void test_getReimbByResolverId_whenUserHasReimb() throws SQLException {
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverId(1)).thenReturn(reimbursements);
        Assert.assertEquals(reimbursements, reimbursementService.getReimbByResolver(1));
    }

    //-----------------------------------------------------------

    @Test
    public void test_save_badReimb(){
        ReimbursementService mock = Mockito.mock(ReimbursementService.class);
        Reimbursement r = new Reimbursement();
        Mockito.when(mock.isReimbursementValid(r)).thenReturn(false);
        Assert.assertFalse(reimbursementService.save(r));
    }

    @Test
    public void test_save_badReimbTwo(){
        ReimbursementService mock = Mockito.mock(ReimbursementService.class);
        Reimbursement r = new Reimbursement();
        Mockito.when(mock.isReimbursementValid(r)).thenReturn(true);
        Mockito.when(reimbursementsRepository.addReimbursement(r)).thenReturn(false);
        Assert.assertFalse(reimbursementService.save(r));
    }

    @Test
    public void test_save_goodReimb(){
        ReimbursementService mock = Mockito.mock(ReimbursementService.class);
        Reimbursement r = new Reimbursement();
        Mockito.when(mock.isReimbursementValid(r)).thenReturn(true);
        Mockito.when(reimbursementsRepository.addReimbursement(r)).thenReturn(true);
        Assert.assertTrue(reimbursementService.save(r));
    }

    //-----------------------------------------------------------
}
