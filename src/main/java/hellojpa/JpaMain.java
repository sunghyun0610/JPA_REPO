package hellojpa;

import jakarta.persistence.*;

import java.util.List;


public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");//데이터베이스 당 하나씩 묶여서 돌아감.
        //entityMangerFactory 만드는 순간 DB연결은 가능
        EntityManager em = emf.createEntityManager();//공장에서 entitymanager꺼내는 코드
            //em을 자바컬렉션처럼 이해하면 좋음, 내 객체를 대신 저장해주는 것.
            EntityTransaction tx = em.getTransaction();//JPA 는 transaction이 매우 중요하다. 트랜잭션 얻는 코드
            tx.begin();
            try {
                Team team = new Team();
                team.setName("Team A");
                em.persist(team);

                Member member = new Member();
//                member.setId("ID_AA");
                member.setUsername("A");
                member.setTeam(team);
                em.persist(member);

                em.flush();
                em.clear();

                Member findMember= em.find(Member.class, member.getId());
                List<Member> members = findMember.getTeam().getMembers();//반대방향으로도 객체 그래프 탐색가능. 즉 양방향 매핑 가능.

                for(Member m : members){
                    System.out.println("m = "+ m.getUsername());
                }

                tx.commit(); // 트랜잭션이 커밋(commit)되는 시점에 데이터베이스에 반영됩니다.
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();//entity manger 꼭 닫아줄 것!
        }
        emf.close();

    }
}

