package mutsa.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.order.OrderDetailResponseDto;
import mutsa.api.dto.order.OrderResponseDto;
import mutsa.api.dto.order.OrderStatueRequestDto;
import mutsa.api.service.order.OrderService;
import mutsa.api.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/articles/{articleApiId}/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * @param articleApiId
     * @param orderApiId
     * @return 주문 단건 조회
     */
    @GetMapping("/{orderApiId}")
    public ResponseEntity<OrderDetailResponseDto> getDetailOrder(
            @PathVariable("articleApiId") String articleApiId,
            @PathVariable("orderApiId") String orderApiId) {
        OrderDetailResponseDto dto = orderService.findDetailOrder(articleApiId, orderApiId, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(dto);
    }

    /**
     * @param articleApiId article apiID
     * @return 게시글의 주문 모두 조회(판매자만 가능)
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getAllOrder(
            @PathVariable("articleApiId") String articleApiId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        Page<OrderResponseDto> dtos = orderService.findAllOrder(articleApiId, page, limit, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(dtos);
    }

    /**
     * @param articleApiId
     * @return 주문 생성
     */
    @PostMapping
    public ResponseEntity<OrderDetailResponseDto> saveOrder(
            @PathVariable("articleApiId") String articleApiId) {
        OrderDetailResponseDto dto = orderService.saveOrder(articleApiId, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(dto);
    }

    /**
     * @param articleApiId
     * @param orderApiId
     * @param orderStatueRequestDto
     * @return 주문 수정
     */
    @PutMapping("/{orderApiId}")
    public ResponseEntity<OrderDetailResponseDto> updateOrderStatus(
            @PathVariable("articleApiId") String articleApiId,
            @PathVariable("orderApiId") String orderApiId,
            @RequestBody OrderStatueRequestDto orderStatueRequestDto) {
        OrderDetailResponseDto dto = orderService.updateOrderStatus(articleApiId, orderApiId, orderStatueRequestDto, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{orderApiId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable("articleApiId") String articleApiId,
            @PathVariable("orderApiId") String orderApiId) {
        orderService.deleteOrder(articleApiId, orderApiId, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok("삭제 완료");
    }
}
