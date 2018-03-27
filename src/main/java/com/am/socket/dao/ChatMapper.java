package com.am.socket.dao;

import com.am.socket.model.OfflineMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatMapper {
    void insertMessageIntoOfflineMessage(OfflineMessage offline);
    List<OfflineMessage> findMessageFromOfflineMessage(@Param("receiverId") int receiverId, @Param("receiveState") int receiveState);
    void updateSendStateOfOfflineMessage(OfflineMessage offline);
}
