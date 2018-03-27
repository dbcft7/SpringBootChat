package com.am.socket.dao;
import com.am.socket.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MomentMapper {
    void insertMomentIntoMoment(Moment moment);
    void deleteMomentFromMoment(Moment moment);
    List<Moment> findMomentsFromMoment(@Param("userId") int userId);
    List<Moment> findFriendMomentsFromMoment(List<Integer> userId);
}
