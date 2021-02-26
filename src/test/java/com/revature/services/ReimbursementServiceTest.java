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
        reimbursementService = null;
    }

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
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndStatus(1, 1)).thenReturn(new LinkedList<>());
        Assert.assertNull(reimbursementService.getReimbByResolverAndStatus(1, 1));
    }

    @Test
    public void test_getReimbByResolverAndType_whenListHasReimb() throws SQLException {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        reimbursementList.add(new Reimbursement());
        Mockito.when(reimbursementsRepository.getAllReimbSetByResolverIdAndType(1, 1)).thenReturn(reimbursementList);
        Assert.assertEquals(reimbursementList, reimbursementService.getReimbByResolverAndType(1, 1));
    }
}
