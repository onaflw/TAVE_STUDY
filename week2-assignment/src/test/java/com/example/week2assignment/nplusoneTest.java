package com.example.week2assignment;

import com.example.week2assignment.domain.Member;
import com.example.week2assignment.domain.Order;
import com.example.week2assignment.repository.MemberRepository;
import com.example.week2assignment.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class NPlusOneTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager em;

    @Test
    void nPlusOneTest() {

        // 회원 생성
        Member member1 = new Member("철수");
        Member member2 = new Member("영희");
        Member member3 = new Member("민수");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // 주문 생성
        orderRepository.save(new Order("맥북", member1));
        orderRepository.save(new Order("아이폰", member2));
        orderRepository.save(new Order("에어팟", member3));

        em.flush();
        em.clear();



        var orders = orderRepository.findAll();

        for (Order order : orders) {
            System.out.println(
                    order.getMember().getName()
            );
        }
    }

    @Test
    void fetchJoinTest() {

        // 회원 생성
        Member member1 = new Member("철수");
        Member member2 = new Member("영희");
        Member member3 = new Member("민수");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // 주문 생성
        orderRepository.save(new Order("맥북", member1));
        orderRepository.save(new Order("아이폰", member2));
        orderRepository.save(new Order("에어팟", member3));

        em.flush();
        em.clear();

        var orders = orderRepository.findAllWithFetchJoin();

        for (Order order : orders) {
            System.out.println(order.getMember().getName());
        }
    }

    @Test
    void entityGraphTest() {

        Member member1 = new Member("철수");
        Member member2 = new Member("영희");
        Member member3 = new Member("민수");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        orderRepository.save(new Order("맥북", member1));
        orderRepository.save(new Order("아이폰", member2));
        orderRepository.save(new Order("에어팟", member3));

        em.flush();
        em.clear();

        var orders = orderRepository.findAllWithEntityGraph();

        for (Order order : orders) {
            System.out.println(order.getMember().getName());
        }
    }
}
