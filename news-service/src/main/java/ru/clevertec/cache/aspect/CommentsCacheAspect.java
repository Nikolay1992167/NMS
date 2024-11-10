package ru.clevertec.cache.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.factory.CacheAlgorithmFactory;
import ru.clevertec.dto.response.ResponseCommentNews;

import java.util.UUID;

@Aspect
@Component
@ConditionalOnBean(CacheAlgorithmFactory.class)
public class CommentsCacheAspect extends AbstractCacheAspect<UUID, ResponseCommentNews> {

    public CommentsCacheAspect(CacheAlgorithmFactory algorithmFactory) {
        super(algorithmFactory);
    }

    @Pointcut(value = "execution(public * ru.clevertec.service.CommentService.find*(..)) && args(id)", argNames = "id")
    void getMethodPointcut(UUID id) {
    }

    @Pointcut(value = "execution(public * ru.clevertec.service.CommentService.create*(..))")
    void createMethodPointcut() {
    }

    @Pointcut(value = "execution(public * ru.clevertec.service.CommentService.update*(..))")
    void updateMethodPointcut() {
    }

    @Pointcut(value = "execution(public * ru.clevertec.service.CommentService.delete*(..)) && args(id)", argNames = "id")
    void deleteMethodPointcut(UUID id) {
    }

    @Around(value = "ru.clevertec.cache.aspect.AnnotationPointCut.cacheGetPointCut() &&" +
            "getMethodPointcut(id)", argNames = "pjp,id")
    public Object getCached(ProceedingJoinPoint pjp, UUID id) {
        return super.getCached(pjp, id);
    }

    @Override
    @AfterReturning(value = "ru.clevertec.cache.aspect.AnnotationPointCut.cacheEvictPointCut() &&" +
            "deleteMethodPointcut(id)", argNames = "id")
    public void deleteFromCache(UUID id) {
        super.deleteFromCache(id);
    }

    @AfterReturning(value = "ru.clevertec.cache.aspect.AnnotationPointCut.cachePutPointCut()" +
            " && createMethodPointcut() || updateMethodPointcut()", returning = "retVal")
    public void putToCache(ResponseCommentNews retVal) {
        super.putToCache(retVal.id(), retVal);
    }
}
